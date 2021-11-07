package symbolicexecution;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.microsoft.z3.*;
import com.microsoft.z3.enumerations.Z3_decl_kind;

import symbolicexecution.exceptions.*;
import symbolicexecution.sqlite.type.SQLiteTypeFactory;
import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLAnyType.Type;
import symbolicexecution.sqltype.SQLBool;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLRowValue;
import symbolicexecution.sqltype.SQLTypeFactory;

public class SymbolicExecution {
	/** Z3 Context */
	protected final com.microsoft.z3.Context ctx;
	/** Z3 Solver */
	protected final com.microsoft.z3.Solver solver;
	/** HashMap from Strings to SQL Types */
	protected HashMap<String, SQLAnyType> z3refs;
	protected TreeSet<String> permanentRefs;
	/** SQL Type Factory **/
	protected SQLTypeFactory factory;
	private int refCounter;
	private int pushCounter;
	protected final String doNotCopyPrefix = "__special__";
	
	/**
	 * Symbol constants for expressions.
	 */
	public static final int SYMBOL_EQ = 0x01, SYMBOL_NEQ = 0x02, SYMBOL_IS = 0x03,
			SYMBOL_LT = 0x11, SYMBOL_LEQ = 0x12, SYMBOL_GT = 0x13, SYMBOL_GEQ = 0x14,
			SYMBOL_ADD = 0x21, SYMBOL_SUB = 0x22, SYMBOL_MULT = 0x23, SYMBOL_DIV = 0x24, SYMBOL_MOD = 0x25,
			SYMBOL_AND = 0x31, SYMBOL_OR = 0x32,
			SYMBOL_MINUS = 0x41, SYMBOL_BITWISE_NOT = 0x42, SYMBOL_NOT = 0x43, SYMBOL_ISNULL = 0x44, SYMBOL_ISNOTNULL = 0x45;
	
	/**
	 * Constructor.
	 * @param databaseName Determines which {@link SQLTypeFactory} to use. Currently supported: "sqlite"
	 * @throws SymbolNotRecognizedException If {@code databaseName} not recognized
	 */
 	public SymbolicExecution(String databaseName) {
		this.ctx = new Context();	// No Model generation enabled.
		this.solver = ctx.mkSolver();
		this.z3refs = new HashMap<String, SQLAnyType>();
		this.permanentRefs = new TreeSet<String>();
		this.refCounter = 0;
		this.pushCounter = 0;
		switch(databaseName.toLowerCase(Locale.ROOT)) {
		case "sqlite":
			this.factory = new SQLiteTypeFactory(this.ctx);
			break;
		default:
			throw new SymbolNotRecognizedException(String.format("Database \"%s\" not recognized by class SymbolicExecution", databaseName));
		}
		
	}
 	public SymbolicExecution(Context ctx) {
 		this.ctx = ctx;
 		solver = null;
 		z3refs = null;
 		refCounter = 0;
 		pushCounter = 0;
 		factory = null;
 	}
	
 	/**
 	 * Creates an integer constant.
 	 * @param number Integer constant
 	 * @return {@link symbolicexecution.Fact} with the reference to the integer constant
 	 */
	public Fact nummericLiteral(long number) {
		String ref = newRef("__lit__");
		SQLInteger expr = factory.makeIntegerLiteral(number);
		z3refs.put(ref, expr);
		int status = (number == 0L) ? Fact.ZERO : Fact.NONZERO;
		if (number == 1L)
			status |= Fact.ONE;
		return new Fact(ref, status, Fact.Type.LITERAL_INT);
	}
	
	/**
 	 * Creates an integer constant.
 	 * @param Integer constant as String
 	 * @return {@link symbolicexecution.Fact} with the reference to the integer constant
 	 */
	public Fact nummericLiteral(String number) {
		String ref = newRef("__lit__");
		SQLAnyType expr = factory.makeNummericLiteral(number);
		z3refs.put(ref, expr);
		int status = checkStatus(expr);
		return new Fact(ref, status, Fact.Type.LITERAL_INT);
	}
	
	/**
 	 * Creates an integer.
 	 * @return {@link symbolicexecution.Fact} with the reference to the integer
 	 */
	public Fact createInteger() {
		String ref = newRef("__int__");
		SQLInteger expr = factory.makeInteger(ref);
		z3refs.put(ref, expr);
		return new Fact(ref, Fact.OK, Fact.Type.INT);
	}
	
	/**
	 * Creates a constant NULL.
	 * @return {@link symbolicexecution.Fact} with the reference to NULL
	 */
	public Fact createNullLiteral() {
		String ref = newRef("__constnull__");
		SQLAnyType expr = factory.makeNullLiteral();
		z3refs.put(ref, expr);
		return new Fact(ref, Fact.JUST_NULL, Fact.Type.LITERAL_NULL);
	}
	
	public Fact createRowValue(List<String> elements) {
		String ref = newRef("__rowvalue__");
		List<SQLAnyType> sqlTypes = elements.stream().map(elem -> exprFromString(elem)).collect(Collectors.toList());
		SQLRowValue expr = factory.makeRowValue(sqlTypes);
		
		// TODO
		return null;
	}
	
	/**
	 * Creates a new stack on the Z3 solver and adds the given expression.
	 * @param reference Boolean expression reference to be added to the solver
	 * @return Integer which can be used to reset the solver stack to the previous state by using {@code pushConstraint()}
	 */
	public int pushConstraint(String reference) {
		SQLBool bool = exprFromString(reference).convertToBoolType();
		solver.push();
		solver.add(ctx.mkAnd(bool.getBoolExpr(), ctx.mkNot(bool.getNullExpr())));
		return this.pushCounter++;
	}
	
	/**
	 * Resets the Z3 solver to the stack given with the argument.
	 * @param pushCounter
	 * @throws RuntimeException If {@code pushCounter < 0} or {@code pushCounter > getPushCounter()}
	 */
	public void resetConstraints(int pushCounter) {
		if (pushCounter < 0 || pushCounter > this.pushCounter)
			throw new RuntimeException("Invalid argument at method 'resetConstraints'.");
		if (pushCounter == this.pushCounter)
			return;
		solver.pop(this.pushCounter - pushCounter);
		this.pushCounter = pushCounter;
	}
	
	/**
	 * Get the current solver stack.
	 */
	public int getPushCounter() {
		return this.pushCounter;
	}
	
	
	/**
	 * Creates an unary expression.
	 * @param operand Reference of operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the unary expression
	 */
	public Fact unaryOperation(String operand, int op_symbol) {
		SQLAnyType expr = exprFromString(operand);
		SQLAnyType resultExpr;
		String ref = newRef("__unaryOP__");
		int status;
		
		switch(op_symbol) {
		case SYMBOL_MINUS:
			resultExpr = expr.negation();
			break;
		case SYMBOL_BITWISE_NOT:
			resultExpr = expr.bitwiseNot();
			break;
		case SYMBOL_NOT:
			resultExpr = expr.not();
			break;
		case SYMBOL_ISNULL:
			resultExpr = expr.isNull();
			break;
		case SYMBOL_ISNOTNULL:
			resultExpr = expr.isNotNull();
			break;
		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + String.valueOf(op_symbol));
		}
		
		z3refs.put(ref, resultExpr);
		status = checkStatus(resultExpr);
		
		return new Fact(ref, status, checkType(resultExpr));
	}
	
	/**
	 * Creates an unary expression.
	 * Supported symbols: -, +, ~, NOT
	 * "+" is treated as a no-op and the initial operand returned
	 * @param operand Reference of operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the unary expression
	 */
	public Fact unaryOperation(String operand, String op_symbol) {
		switch(op_symbol.toUpperCase(Locale.ROOT)) {
		case "-":
			return unaryOperation(operand, SYMBOL_MINUS);
		case "+": // Basicly a no-op
			return new Fact(operand, Fact.OK, checkType(exprFromString(operand)));
		case "~":
			return unaryOperation(operand, SYMBOL_BITWISE_NOT);
		case "NOT":
			return unaryOperation(operand, SYMBOL_NOT);
		case "ISNULL":
			return unaryOperation(operand, SYMBOL_ISNULL);
		case "NOTNULL":
		case "NOT NULL":
			return unaryOperation(operand, SYMBOL_ISNOTNULL);
		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + op_symbol);
		}
	}
	
	/**
	 * Creates a binary expression.
	 * @param operand_left Reference of left operand
	 * @param operand_right Reference of right operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the binary expression
	 */
	public Fact binaryOperation(String operand_left, String operand_right, int op_symbol) {
		SQLAnyType exp1 = exprFromString(operand_left), exp2 = exprFromString(operand_right);
		SQLAnyType resultExpr;
		String ref = newRef("__binOP__");
		int status = 0;
		
		switch (op_symbol) {
		case SYMBOL_EQ:
			resultExpr = exp1.eq(exp2);
			break;
		case SYMBOL_NEQ:
			resultExpr = exp1.neq(exp2);
			break;
		case SYMBOL_LT:
			resultExpr = exp1.lt(exp2);
			break;
		case SYMBOL_LEQ:
			resultExpr = exp1.leq(exp2);
			status = checkInequality(exp1, exp2, op_symbol);
			break;
		case SYMBOL_GT:
			resultExpr = exp1.gt(exp2);
			break;
		case SYMBOL_GEQ:
			resultExpr = exp1.geq(exp2);
			status = checkInequality(exp1, exp2, op_symbol);
			break;
		case SYMBOL_ADD:
			resultExpr = exp1.add(exp2);
			break;
		case SYMBOL_SUB:
			resultExpr = exp1.sub(exp2);
			break;
		case SYMBOL_MULT:
			resultExpr = exp1.mul(exp2);
			break;
		case SYMBOL_DIV:
			resultExpr = exp1.div(exp2);
			break;
		case SYMBOL_MOD:
			resultExpr = exp1.mod(exp2);
			break;
		case SYMBOL_AND:
			resultExpr = exp1.and(exp2);
			break;
		case SYMBOL_OR:
			resultExpr = exp1.or(exp2);
			status = checkOr(exp1, exp2);
			break;
		case SYMBOL_IS:
			resultExpr = exp1.is(exp2);
			break;
		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + String.valueOf(op_symbol));
		}
		
		z3refs.put(ref, resultExpr);
		status |= checkStatus(resultExpr);
		
		if (resultExpr.getType() == Type.INTEGER)
			status |= checkIntegerExactlySubexpression(resultExpr.convertToIntegerType(), exp1.convertToIntegerType(), exp2.convertToIntegerType());
		
		return new Fact(ref, status, checkType(resultExpr));
	}
	
	/**
	 * Creates a binary expression.
	 * Supported symbols:
	 * =, ==, !=, <>, <, <=, >, >=, +, -, *, /, %, AND, OR
	 * @param operand_left Reference of left operand
	 * @param operand_right Reference of right operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the binary expression
	 */
	public Fact binaryOperation(String operand_left, String operand_right, String op_symbol) {
		switch (op_symbol.toUpperCase(Locale.ROOT)) {
		case "=":
		case "==":
			return binaryOperation(operand_left, operand_right, SYMBOL_EQ);
		case "!=":
		case "<>":
			return binaryOperation(operand_left, operand_right, SYMBOL_NEQ);
		case "<":
			return binaryOperation(operand_left, operand_right, SYMBOL_LT);
		case "<=":
			return binaryOperation(operand_left, operand_right, SYMBOL_LEQ);
		case ">":
			return binaryOperation(operand_left, operand_right, SYMBOL_GT);
		case ">=":
			return binaryOperation(operand_left, operand_right, SYMBOL_GEQ);
		case "+":
			return binaryOperation(operand_left, operand_right, SYMBOL_ADD);
		case "-":
			return binaryOperation(operand_left, operand_right, SYMBOL_SUB);
		case "*":
			return binaryOperation(operand_left, operand_right, SYMBOL_MULT);
		case "/":
			return binaryOperation(operand_left, operand_right, SYMBOL_DIV);
		case "%":
			return binaryOperation(operand_left, operand_right, SYMBOL_MOD);
		case "AND":
			return binaryOperation(operand_left, operand_right, SYMBOL_AND);
		case "OR":
			return binaryOperation(operand_left, operand_right, SYMBOL_OR);
		case "IS":
			return binaryOperation(operand_left, operand_right, SYMBOL_IS);

		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + op_symbol);
		}
	}
	
	/**
	 * Creates an SQL case operation.
	 * @param whenConditionRefList Reference List of the case distinction conditions
	 * @param thenRefList Reference List containing the resulting expressions for the corresponding case distinction
	 * @param elseExpression Optional else expression reference. Can be {@code null} to ommit the else case 
	 * @return {@link SymbolicExecution.Fact} with the reference to the case expression
	 * @throws RuntimeException when the parameter lists do not have the same size
	 */
	public Fact caseOperation(List<String> whenConditionRefList, List<String> thenRefList, String elseExpression) {
		if (whenConditionRefList.size() != thenRefList.size())
			throw new RuntimeException();
		
		ListIterator<String> whenIter = whenConditionRefList.listIterator(whenConditionRefList.size());
		ListIterator<String> thenIter = thenRefList.listIterator(thenRefList.size());
		SQLAnyType previous;
		if (elseExpression == null) 
			previous = factory.makeNullLiteral();
		else
			previous = exprFromString(elseExpression);
		
		while (whenIter.hasPrevious()) {
			SQLAnyType cond = exprFromString(whenIter.previous());
			SQLAnyType then = exprFromString(thenIter.previous());
			previous = cond.iif(then, previous);
		}
		
		String ref = newRef("__case__");
		z3refs.put(ref, previous);
		return new Fact(ref, checkStatus(previous), checkType(previous));
	}
	
	/**
	 * Creates a deep copy of the given references.
	 * Can be used to e.g. copy a table.
	 * @param refsToCopy Array containing the references
	 * @return Array with the reference copies
	 */
	public String[] createReferenceCopies(String[] refsToCopy) {
		SQLAnyType[] expr = new SQLAnyType[refsToCopy.length];
		for (int i = 0; i < refsToCopy.length; i++) {
			expr[i] = exprFromString(refsToCopy[i]);
		}
		TreeSet<Expr> set = new TreeSet<Expr>();
		for (SQLAnyType single : expr) {
			for (Expr singleExpr : single.getAllExpr()) {
				extractConstDeclarations(singleExpr, set);
			}
		}
		Expr[] from = set.toArray(new Expr[0]);
		Expr[] to = copyZ3Consts(from);
		String[] result = new String[expr.length];
		for (int i = 0; i < expr.length; i++) {
			SQLAnyType temp = expr[i].substitute(from, to);
			String tempRef = newRef("__copy__");
			this.z3refs.put(tempRef, temp);
			result[i] = tempRef;
		}
		
		return result;
	}
	
	/**
	 * Creates a copy for each Z3 expr where each Z3 const obtains a new unique name while keeping the same sort.
	 * @param expr
	 * @return Array with the copies of {@code expr}
	 */
	protected Expr[] copyZ3Expr(Expr... expr) {
		if (expr == null)
			return null;
		TreeSet<Expr> set = new TreeSet<Expr>();
		for (Expr single : expr) {
			extractConstDeclarations(single, set);
		}
		Expr[] consts = set.toArray(new Expr[0]);
		Expr[] replacement = copyZ3Consts(consts);
		Expr[] result = new Expr[expr.length];
		for (int i = 0; i < expr.length; i++) {
			result[i] = expr[i].substitute(consts, replacement);
		}
		return result;
	}
	
	/**
	 * Creates a copy for each Z3 const with a new unique name and the same sort.
	 * @param expr
	 * @return Array with the copies of {@code expr}
	 */
	private Expr[] copyZ3Consts(Expr... expr) {
		int size = expr.length;
		Expr[] result = new Expr[size];
		for (int i = 0; i < size; i++) {
			String ref = newRef("__tmp__");
			Sort sort = expr[i].getSort();
			result[i] = ctx.mkConst(ref, sort);
		}
		return result;
	}
	
	/**
	 * Extracts all Z3 const declarations for a given expression and inserts them into the given set.
	 * @param expr Z3 expression to extract const declarations from
	 * @param set The set all found const declarations are inserted to
	 */
	private void extractConstDeclarations(Expr expr, TreeSet<Expr> set) {
		if (expr.isConst() && expr.getFuncDecl().getDeclKind() == Z3_decl_kind.Z3_OP_UNINTERPRETED 
				&& !expr.toString().startsWith(this.doNotCopyPrefix)) {
			set.add(expr);
		}
		else {
			for (Expr subexpr: expr.getArgs())
				extractConstDeclarations(subexpr, set);
		}
	}
	
	/**
	 * Returns the Z3 Expr associated with key ref.
	 * @param ref Key for the internal {@link HashMap}
	 * @return {@link symbolicexecution.sqltype} object
	 * @throws ReferenceNotFoundException if key is not found.
	 */
	protected SQLAnyType exprFromString(String ref) {
		SQLAnyType expr = z3refs.get(ref);
		if (expr == null) {
			throw new ReferenceNotFoundException(ref);
		}
		return expr;
	}
	
	/**
	 * Adds an integer to the String prefix, which increases each time this method is called.
	 * @param prefix String prefix
	 * @return String as a combination of prefix and an unique number
	 */
	protected String newRef(String prefix) {
		return prefix + String.valueOf(refCounter++);
	}
	
	/**
	 * Uses the Z3 solver to check the satisfiability of an expression
	 * @param expr Z3 expression
	 * @return {@code true} if satisfiable, {@code false} otherwise
	 * @throws UnknownSolverResult if the solver is unable to determine satisfiability
	 */
	protected boolean checkSat(Expr expr) {
		Status status = this.solver.check(expr);
		switch(status) {
		case SATISFIABLE:
			return true;
		case UNSATISFIABLE:
			return false;
		default:
			throw new UnknownSolverResult();
		}
	}
	
	/**
	 * Determines the status of a Boolean expression.
	 * Sets the Flags {@code Fact.JUST_NULL}, {@code Fact.TAUTOLOGY}, {@code Fact.UNSATISFIABLE} and {@code Fact.MAY_CONTAIN_NULL}.
	 * @param expr
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkStatusBool(SQLBool expr) {
		int status = Fact.OK;
		boolean notOnlyNull = checkSat(ctx.mkNot(expr.getNullExpr()));
		if (!notOnlyNull) {
			return Fact.JUST_NULL;
		}
		boolean satisfiable = checkSat(expr.getBoolExpr());
		boolean negationSatisfiable = checkSat(ctx.mkNot(expr.getBoolExpr()));
		boolean mayBeNull = checkSat(expr.getNullExpr());
		if (satisfiable && !negationSatisfiable && !mayBeNull)
			status |= Fact.TAUTOLOGY;
		else if (!satisfiable && negationSatisfiable)
			status |= Fact.UNSATISFIABLE;
		if (mayBeNull)
			status |= Fact.MAY_CONTAIN_NULL;
		return status;
	}
	
	/**
	 * Determines the status of an Integer expression.
	 * Sets the Flags {@code Fact.JUST_NULL}, {@code Fact.ZERO}, {@code Fact.NONZERO}, {@code Fact.ONE}, {@code Fact.MAY_CONTAIN_NULL} and {@code Fact.CONSTANT}.
	 * @param expr
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkStatusInt(SQLInteger expr) {
		int status = Fact.OK;
		boolean notOnlyNull = checkSat(ctx.mkNot(expr.getNullExpr()));
		if (!notOnlyNull) {
			return Fact.JUST_NULL;
		}
		boolean mayBeZero = checkSat(ctx.mkEq(expr.getIntExpr(), ctx.mkInt(0)));
		boolean mayBeNonzero = checkSat(ctx.mkNot(ctx.mkEq(ctx.mkITE(expr.getNullExpr(), ctx.mkInt(0), expr.getIntExpr()), ctx.mkInt(0))));
		boolean mayNotBeOne = checkSat(ctx.mkNot(ctx.mkEq(expr.getIntExpr(), ctx.mkInt(1))));
		boolean mayBeNull = checkSat(expr.getNullExpr());
		boolean isConstant = checkConstant(expr.getIntExpr(), true);
		if (mayBeZero && !mayBeNonzero)
			status |= Fact.ZERO;
		else if (!mayBeZero && mayBeNonzero)
			status |= Fact.NONZERO;
		if (!mayNotBeOne)
			status |= Fact.ONE;
		if (mayBeNull)
			status |= Fact.MAY_CONTAIN_NULL;
		if (isConstant)
			status |= Fact.CONSTANT;
		return status;
	}
	
	/**
	 * Determines whether the left or right expression is strictly equal to the resulting expression.
	 * An example could be the binary operation result = x * 1, where result and x are strictly equal.
	 * Sets the Flags {@code Fact.LEFT_UNNECESSARY} and {@code Fact.RIGHT_UNNECESSARY}.
	 * @param result Result of the binary operation
	 * @param left Left expression
	 * @param right Right expression
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkIntegerExactlySubexpression(SQLInteger result, SQLInteger left, SQLInteger right) {
		BoolExpr rightUnnecessary = result.isNot(left).convertToBoolType().getBoolExpr();
		BoolExpr leftUnnecessary = result.isNot(right).convertToBoolType().getBoolExpr();
		boolean rightTest = !checkSat(rightUnnecessary);
		boolean leftTest = !checkSat(leftUnnecessary);
		
		if (rightTest)
			return Fact.RIGHT_UNNECESSARY;
		else if (leftTest)
			return Fact.LEFT_UNNECESSARY;
		return 0;
	}
	
	/**
	 * Calls the appropiate status check method depending on the SQL type of the expression.
	 * @param expr
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkStatus(SQLAnyType expr) {
		switch(expr.getType()) {
		case BOOL:
			return checkStatusBool((SQLBool) expr);
		case INTEGER:
			return checkStatusInt((SQLInteger) expr);
		// TODO: Exhaust all possibilities, get rid of default
		default:
			return 0;
		}
	}
	
	protected Fact.Type checkType(SQLAnyType expr) {
		switch(expr.getType()) {
		case BOOL:
			return Fact.Type.BOOL;
		case INTEGER:
			return Fact.Type.INT;
		// TODO: Exhaust all possibilities
		default:
			return Fact.Type.UNKNOWN;
		}
	}
	
	/**
	 * Determines for expressions containing inequalities (<=, >=) whether the equality or inequality is strict.
	 * Sets the Flags {@code Fact.EQUALITY_UNNECESSARY} and {@code Fact.JUST_EQUALITY}.
	 * @param expr1 Left expression
	 * @param expr2 Right expression
	 * @param op_symbol Operator
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkInequality(SQLAnyType expr1, SQLAnyType expr2, int op_symbol) {
		SQLBool equality = (SQLBool) expr1.eq(expr2);
		if (!checkSat(equality.getBoolExpr()))
			return Fact.EQUALITY_UNNECESSARY;
		
		SQLBool inequality;
		if (op_symbol == SymbolicExecution.SYMBOL_GEQ)
			inequality = (SQLBool) expr1.gt(expr2);
		else
			inequality = (SQLBool) expr1.lt(expr2);
		if (!checkSat(inequality.getBoolExpr()))
			return Fact.JUST_EQUALITY;
		
		return 0;
	}
	
	/**
	 * Determines for an OR expression whether the left side implies the right side (or vice versa).
	 * Sets the Flags {@code Fact.LEFT_UNNECESSARY} and {@code Fact.RIGHT_UNNECESSARY}.
	 * @param exp1 Left expression
	 * @param exp2 Right expression
	 * @return Integer containing the flags set accordingly to {@link Fact}
	 * @throws UnknownSolverResult
	 */
	protected int checkOr(SQLAnyType exp1, SQLAnyType exp2) {
		SQLBool e1 = exp1.convertToBoolType();
		SQLBool e2 = exp2.convertToBoolType();
		
		BoolExpr bothNotEqual = ctx.mkOr(ctx.mkXor(e1.getBoolExpr(), e2.getBoolExpr()), ctx.mkXor(e1.getNullExpr(), e2.getNullExpr()));
		BoolExpr leftIsTrue = ctx.mkAnd(e1.getBoolExpr(), ctx.mkNot(e1.getNullExpr()));
		BoolExpr rightIsTrue = ctx.mkAnd(e2.getBoolExpr(), ctx.mkNot(e2.getNullExpr()));
		boolean leftImpliesNotRight = checkSat(ctx.mkAnd(leftIsTrue, bothNotEqual));
		boolean rightImpliesNotLeft = checkSat(ctx.mkAnd(rightIsTrue, bothNotEqual));
		
		if (!leftImpliesNotRight)
			return Fact.LEFT_UNNECESSARY;
		if (!rightImpliesNotLeft)
			return Fact.RIGHT_UNNECESSARY;
		return 0;
	}
	
	/**
	 * Checks whether the given integer expression is constant.
	 * @param expr Z3 integer expression
	 * @param includeSolverContent Whether to consider everything pushed to the Z3 solver
	 * @return true iff {@code expr} is constant
	 * @throws UnknownSolverResult 
	 */
	protected boolean checkConstant(Expr expr, boolean includeSolverContent) {
		// This operation could be quite time consuming
		// An alternative way is to enable model generation and check twice, 
		// excluding the previous value of the first run in the second run
		BoolExpr original = ctx.mkEq(ctx.mkConst(newRef(this.doNotCopyPrefix), expr.getSort()), expr);
		BoolExpr copy = ctx.mkNot(original);
		if (includeSolverContent) {
			BoolExpr solCont = ctx.mkAnd(solver.getAssertions());
			copy = ctx.mkAnd(solCont, copy);
		}
		copy = (BoolExpr) copyZ3Expr(copy)[0];
		boolean sat = checkSat(ctx.mkAnd(original, copy));
		return !sat;
	}
	
	/**
	 * Disposes the Z3 Context.
	 */
	public void close() {
		ctx.close();
		z3refs.clear();
	}
}
