package symbolicexecution;

import java.util.HashMap;

import com.microsoft.z3.*;

import symbolicexecution.exceptions.*;

public class SymbolicExecution {
	/** Z3 Context */
	protected final com.microsoft.z3.Context ctx;
	/** Z3 Solver */
	protected final com.microsoft.z3.Solver solver;
	/** HashMap from Strings to Z3 Expr */
	protected HashMap<String, com.microsoft.z3.Expr> z3refs;
	private int refCounter;
	
	/** 
	 * Status constants of the execution.
	 * 
	 */
	public static final int STATUS_OK = 0, 
			STATUS_TAUTOLOGY = 1, STATUS_UNSATISFIABLE = 2,
			STATUS_ZERO = 4, STATUS_ONE = 8, STATUS_NONZERO = 16;
	
	/**
	 * Symbol constants for expressions.
	 */
	public static final int SYMBOL_EQ = 0x01, SYMBOL_NEQ = 0x02, 
			SYMBOL_LT = 0x11, SYMBOL_LEQ = 0x12, SYMBOL_GT = 0x13, SYMBOL_GEQ = 0x14,
			SYMBOL_PLUS = 0x21, SYMBOL_MINUS = 0x22, SYMBOL_MULT = 0x23, SYMBOL_DIV = 0x24, SYMBOL_MOD = 0x25;
	
 	public SymbolicExecution() {
		this.ctx = new Context();	// No Model generation enabled.
		this.solver = ctx.mkSolver();
		this.z3refs = new HashMap<String, com.microsoft.z3.Expr>();
		this.refCounter = 0;
	}
	
 	/**
 	 * Creates an integer constant.
 	 * @param number Integer constant
 	 * @return {@link symbolicexecution.Fact} with the reference to the integer constant
 	 */
	public Fact nummericLiteral(long number) {
		String ref = newRef("__lit__");
		Expr expr = ctx.mkInt(number);
		z3refs.put(ref, expr);
		int status = (number == 0L) ? STATUS_ZERO : STATUS_NONZERO;
		if (number == 1L)
			status |= STATUS_ONE;
		return new Fact(ref, status);
	}
	
	/**
 	 * Creates an integer constant.
 	 * @param Integer constant as String
 	 * @return {@link symbolicexecution.Fact} with the reference to the integer constant
 	 */
	public Fact nummericLiteral(String number) {
		String ref = newRef("__lit__");
		IntNum expr = ctx.mkInt(number);
		z3refs.put(ref, expr);
		long numberAsLong = expr.getInt64();
		int status = (numberAsLong == 0L) ? STATUS_ZERO : STATUS_NONZERO;
		if (numberAsLong == 1L)
			status |= STATUS_ONE;
		return new Fact(ref, status);
	}
	
	/**
	 * Creates a binary expression.
	 * @param operand_left Reference of left operand
	 * @param operand_right Reference of right operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the binary expression
	 */
	public Fact binaryOperation(String operand_left, String operand_right, int op_symbol) {
		Expr exp1 = exprFromString(operand_left), exp2 = exprFromString(operand_right);
		Fact result;
		
		switch (op_symbol) {
		case SYMBOL_EQ:		// Fallthrough
		case SYMBOL_NEQ:
			result = binaryOperationEquality(exp1, exp2, op_symbol);
			break;
		case SYMBOL_LT:		// Fallthrough
		case SYMBOL_LEQ:
		case SYMBOL_GT:
		case SYMBOL_GEQ:
			result = binaryOperationComparison(exp1, exp2, op_symbol);
			break;
		case SYMBOL_PLUS:	// Fallthrough
		case SYMBOL_MINUS:
		case SYMBOL_MULT:
		case SYMBOL_DIV:
		case SYMBOL_MOD:
			result = binaryOperationArithmetic(exp1, exp2, op_symbol);
			break;
		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + String.valueOf(op_symbol));
		}
		
		return result;
	}
	
	/**
	 * Creates a binary expression.
	 * Supported symbols:
	 * =, ==, !=, <>, <, <=, >, >=, +, -, *, /, %
	 * @param operand_left Reference of left operand
	 * @param operand_right Reference of right operand
	 * @param op_symbol Operant symbol
	 * @return {@link SymbolicExecution.Fact} with the reference to the binary expression
	 */
	public Fact binaryOperation(String operand_left, String operand_right, String op_symbol) {
		if (op_symbol.equals("=") || op_symbol.equals("=="))
			return binaryOperation(operand_left, operand_right, SYMBOL_EQ);
		else if (op_symbol.equals("!=") || op_symbol.equals("<>"))
			return binaryOperation(operand_left, operand_right, SYMBOL_NEQ);
		else if (op_symbol.equals("<"))
			return binaryOperation(operand_left, operand_right, SYMBOL_LT);
		else if (op_symbol.equals("<="))
			return binaryOperation(operand_left, operand_right, SYMBOL_LEQ);
		else if (op_symbol.equals(">"))
			return binaryOperation(operand_left, operand_right, SYMBOL_GT);
		else if (op_symbol.equals(">="))
			return binaryOperation(operand_left, operand_right, SYMBOL_GEQ);
		else if (op_symbol.equals("+"))
			return binaryOperation(operand_left, operand_right, SYMBOL_PLUS);
		else if (op_symbol.equals("-"))
			return binaryOperation(operand_left, operand_right, SYMBOL_MINUS);
		else if (op_symbol.equals("*"))
			return binaryOperation(operand_left, operand_right, SYMBOL_MULT);
		else if (op_symbol.equals("/"))
			return binaryOperation(operand_left, operand_right, SYMBOL_DIV);
		else if (op_symbol.equals("%"))
			return binaryOperation(operand_left, operand_right, SYMBOL_MOD);
		
		throw new SymbolNotRecognizedException("Unknown symbol: " + op_symbol);
	}
	
	/**
	 * Binary operation on equality (== or !=).
	 * @param exp1 Left expression
	 * @param exp2 Right expression
	 * @param op_symbol Operation symbol
	 * @return {@link Fact} with new reference
	 */
	protected Fact binaryOperationEquality(Expr exp1, Expr exp2, int op_symbol) {
		// Vars
		int status = STATUS_OK;
		String ref = newRef("__binEQ__");
		BoolExpr resultExpr, negation;
		
		// Type conversion
		// Precondition
		// Expr generation
		if (op_symbol == SYMBOL_EQ) {
			resultExpr = ctx.mkEq(exp1, exp2);
			negation = ctx.mkNot(resultExpr);
		}
		else {
			negation = ctx.mkEq(exp1, exp2);
			resultExpr = ctx.mkNot(negation);
		}
		z3refs.put(ref, resultExpr);
		
		// Postcondition
		Status resultNotUnsat = solver.check(resultExpr);
		Status resultNotTautology = solver.check(negation);
		if (resultNotUnsat == Status.UNSATISFIABLE) 
			status = STATUS_UNSATISFIABLE;
		else if (resultNotTautology == Status.UNSATISFIABLE)
			status = STATUS_TAUTOLOGY;
		
		return new Fact(ref, status);
	}
	
	/**
	 * Binary operation on comparison (<, <=, >, >=).
	 * @param exp1 Left expression
	 * @param exp2 Right expression
	 * @param op_symbol Operation symbol
	 * @return {@link Fact} with new reference
	 */
	protected Fact binaryOperationComparison(Expr exp1, Expr exp2, int op_symbol) {
		// Vars
		int status = STATUS_OK;
		String ref = newRef("__binComp__");
		BoolExpr resultExpr, negation;
		
		// Type conversion
		ArithExpr e1 = (ArithExpr) exp1, e2 = (ArithExpr) exp2;
		
		// Precondition
		// Expr generation
		switch (op_symbol) {
		case SYMBOL_LT:
			resultExpr = ctx.mkLt(e1, e2);
			negation = ctx.mkGe(e1, e2);
			break;
		case SYMBOL_LEQ:
			resultExpr = ctx.mkLe(e1, e2);
			negation = ctx.mkGt(e1, e2);
			break;
		case SYMBOL_GT:
			resultExpr = ctx.mkGt(e1, e2);
			negation = ctx.mkLe(e1, e2);
			break;
		case SYMBOL_GEQ:
			resultExpr = ctx.mkGe(e1, e2);
			negation = ctx.mkLt(e1, e2);
			break;
		default:
			throw new SymbolNotRecognizedException("Unknown symbol: " + String.valueOf(op_symbol));
		}
		
		// Postcondition
		Status resultNotUnsat = solver.check(resultExpr);
		Status resultNotTautology = solver.check(negation);
		if (resultNotUnsat == Status.UNSATISFIABLE) 
			status = STATUS_UNSATISFIABLE;
		else if (resultNotTautology == Status.UNSATISFIABLE)
			status = STATUS_TAUTOLOGY;
		
		return new Fact(ref, status);
	}
	
	protected Fact binaryOperationArithmetic(Expr exp1, Expr exp2, int op_symbol) {
		// TODO
		return null;
	}
	
	/**
	 * Returns the Z3 Expr associated with key ref.
	 * Throws {@link ReferenceNotFoundException} if key is not found.
	 * @param ref Key for the internal {@link HashMap}
	 * @return Z3 {@link com.microsoft.z3.Expr} object
	 */
	protected Expr exprFromString(String ref) {
		Expr expr = z3refs.get(ref);
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
	 * Disposes the Z3 Context.
	 */
	public void close() {
		ctx.close();
		z3refs.clear();
	}
}
