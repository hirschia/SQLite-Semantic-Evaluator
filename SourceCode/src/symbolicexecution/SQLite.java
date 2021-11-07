package symbolicexecution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.sqlite.SQLiteBaseVisitor;
import parser.sqlite.SQLiteParser;
import parser.sqlite.SQLiteParser.*;
import symbolicexecution.Feedback.Type;
import symbolicexecution.exceptions.ParserElementNotImplemented;
import symbolicexecution.util.Pair;

/**
 * SQLite visitor for the parsed SQLite queries.
 * This class controls the semantic evaluation of SQLite utilizing {@link SymbolicExecution}
 * and generates the feedback messages.
 * 
 * As a rule, every method named visit[grammar_name] corresponds to the rule named [grammar_name] in the file parser/SQLite.g4.
 */
public class SQLite extends SQLiteBaseVisitor<Fact> {
	
	protected final SymbolicExecution se;
	protected final Feedback fb;
//	protected final HashMap<String, String> scopeAlias;
	protected final HashMap<String, Table> tables;
	protected TableScope tablesInScope;
	protected boolean messageGenerationActive;
	private int tempCounter;
	
	public SQLite(Feedback feedback) {
		this.fb = feedback;
		this.se = new SymbolicExecution("SQLite");
//		this.scopeAlias = new HashMap<String, String>();
		this.tablesInScope = new TableScope(null);
		this.tables = new HashMap<String, Table>();
		this.messageGenerationActive = true;
		this.tempCounter = 0;
	}
	
	/**
	 * Generates the message for a semantic error and provides it to the {@code Feedback} interface.
	 * @param ctx ParseTree context responsible for the error, necessary for localizing the error source
	 * @param type Error type
	 * @param additionalArguments Depending on the error type, more information is needed for an accurate message generation
	 */
	protected void generateMessage(ParserRuleContext ctx, ErrorType type, Object... additionalArguments) {
		if (!this.messageGenerationActive)
			return;
		TreeInterval interval = new TreeInterval(ctx);
		String errorExpression = formatTree(ctx);
		String message = "";
		
		int sizeAdditionalArguments = 0;
		if (additionalArguments != null)
			sizeAdditionalArguments = additionalArguments.length;
		switch (type) {
		case COLUMNCONSTRAINT_TAUTOLOGY:
			message = "The column constraints are a tautology. Alter or remove any constraint.";
			break;
		case COLUMNCONSTRAINT_UNSATISFIABLE:
			message = "The column constraints are unsatisfiable, therefore the table is unusable."
					+ "Alter or remove any constraint.";
			break;
		case JOIN_CONSTRAINT_UNSATISFIABLE:
			message = "The join constraint is unsatisfiable.";
			break;
		case JOIN_CONSTRAINTS_COMBINED_UNSATISFIABLE:
			message = "The conjunction of all join constraints is unsatisfiable.";
			break;
		case WHERE_TAUTOLOGY:
			if (sizeAdditionalArguments > 0 && additionalArguments[0] instanceof TerminalNode) {
				TerminalNode where = (TerminalNode) additionalArguments[0];
				errorExpression = formatTree(where) + " " + errorExpression;
				interval = interval.merge(new TreeInterval(where));
			}
			message = "The where condition is a tautology. Remove the where clause.";
			break;
		case WHERE_UNSATISFIABLE:
			if (sizeAdditionalArguments > 0 && additionalArguments[0] instanceof TerminalNode) {
				TerminalNode where = (TerminalNode) additionalArguments[0];
				errorExpression = formatTree(where) + " " + errorExpression;
				interval = interval.merge(new TreeInterval(where));
			}
			message = "The where condition is unsatisfiable. "  
					+ "Correct the where clause or remove the whole statement.";
			break;
		case EXPRESSION_TAUTOLOGY:
			message = "This expression is a tautology. Replace with \"1\".";
			break;
		case EXPRESSION_UNSATISFIABLE:
			message = "Expression is unsatisfiable. Replace with \"0\"";
			break;
		case EXPRESSION_UNSATISFIABLE_OR_NULL:
			message = "Expression is unsatisfiable or NULL.";
			break;
		case EXPRESSION_JUST_ZERO:
		case EXPRESSION_MINUS_ZERO:
			message = "Expression is only \"0\".";
			break;
		case EXPRESSION_TURNED_CONST:
			message = "Expression is a constant.";
			break;
		case LEFT_EXPR_UNNECESSARY:
			String replacement = "";
			if (ctx instanceof Expr_binaryContext) {
				Expr_binaryContext expr = (Expr_binaryContext) ctx;
				replacement = String.format(" Replace this statement with \"%s\".", formatTree(expr.expr(1)));
			}
			message = "The left expression is unnecessary." + replacement;
			break;
		case RIGHT_EXPR_UNNECESSARY:
			replacement = "";
			if (ctx instanceof Expr_binaryContext) {
				Expr_binaryContext expr = (Expr_binaryContext) ctx;
				replacement = String.format(" Replace this statement with \"%s\".", formatTree(expr.expr(0)));
			} // Might want to add more binary expressions?
			message = "The right expression is unnecessary." + replacement;
			break;
		case EQUALITY_IS_STRICT:
			message = "This inequality is a strict equality. Replace operator with \"=\".";
			break;
		case INEQUALITY_IS_STRICT:
			replacement = "";
			if (ctx instanceof Expr_binaryContext) {
				String op = ((Expr_binaryContext) ctx).getChild(1).getText();
				replacement = String.format(" Replace operator with \"%c\".", op.charAt(0));
			}
			message = "This inequality is a strict inequality." + replacement;
			break;
		case EXPRESSION_WHEN_TAUTOLOGY:
			message = "The WHEN case is a tautology. Replace with an ELSE case and remove unreachable following cases.";
			break;
		case EXPRESSION_WHEN_UNSATISFIABLE:
			message = "The WHEN case is unsatisfiable. Change the expression or remove this WHEN case.";
			break;
		case UPDATE_ASSIGNMENT_NOT_IN_RANGE:
			if (sizeAdditionalArguments > 0 && additionalArguments[0] instanceof Column_nameContext) {
				Column_nameContext column = (Column_nameContext) additionalArguments[0];
				errorExpression = formatTree(column) + " = " + errorExpression;
				interval = interval.merge(new TreeInterval(column));
				if (sizeAdditionalArguments > 1 && additionalArguments[1] instanceof TerminalNode) {
					TerminalNode node = (TerminalNode) additionalArguments[1];
					errorExpression = node.getText() + " " + errorExpression;
					interval = interval.merge(new TreeInterval(node));
				}
			}
			message = "This update might result in an error. "
					+ "Adjust the where clause to catch edge cases.";
			break;
		default:
			break;
		}
		
		String finalMessage = errorExpression + "\n" + message + "\n\n";
		this.fb.processFeedback(interval.lineStart, interval.charStart, interval.lineStop, interval.charStop, finalMessage, Feedback.Type.MESSAGE);
	}
	
	protected int loadTableAlias(List<Pair<String, String>> tableList) {
		int pushCounter = se.getPushCounter();
		
		//TODO: push aliastable in Stack if not empty
		
		// Tablename conflicts
		HashSet<String> usedTables = new HashSet<String>(tables.size() * 2);
		HashSet<String> tableNameConflicts = new HashSet<String>(tables.size());
		// Columnname conflicts
		HashSet<String> usedColumnNames = new HashSet<String>();
		HashSet<String> columnNameConflicts = new HashSet<String>();
		for (Pair<String, String> entry: tableList) {
			String name = entry.snd == null ? entry.fst : entry.snd;
			if (!usedTables.add(name)) {
				tableNameConflicts.add(name);
			}
			
			for (TableEntry var: this.tables.get(entry.fst).getEntries()) {
				if (!usedColumnNames.add(var.getColumnName()))
					columnNameConflicts.add(var.getColumnName());
			}
		}
		usedTables.clear();
		usedColumnNames.clear();
		
		this.tablesInScope = new TableScope(this.tablesInScope);
		for (Pair<String, String> entry: tableList) {
			String tableName = (entry.snd == null) ? entry.fst : entry.snd;
			if (tableNameConflicts.contains(tableName))
				continue;
			Table currentTable = this.tables.get(entry.fst);
			if (this.tablesInScope.insertTableUsed(currentTable.getName())) {
				// TODO copy
			}
			for (TableEntry var: this.tables.get(entry.fst).getEntries()) {
				if (!columnNameConflicts.contains(var.name))
//					this.scopeAlias.put(var.getColumnName(), var.getConstraintRef());
					this.tablesInScope.setEntry(var.getColumnName(), var.getRef());
//				this.scopeAlias.put(tableName + "." + var.getColumnName(), var.getConstraintRef());
				this.tablesInScope.setEntry(tableName + "." + var.getColumnName(), var.getRef());
				if (var.hasConstraint()) {
					se.pushConstraint(var.getConstraintRef());
				}
			}
			if (currentTable.hasTableConstraint()) {
				se.pushConstraint(currentTable.getConstraint());
			}
		}
		return pushCounter;
	}
	
	protected void clearTableAlias(int pushCounter) {
		// TODO: load previous table
//		this.scopeAlias.clear();
		this.tablesInScope = this.tablesInScope.clearAndGetPrevious();
		se.resetConstraints(pushCounter);
	}
	
	/**
	 * Finds semantic errors for conjunctions.
	 * @param lst List of expressions in the conjunction
	 * @return {@link Fact} containing the reference to all expressions joined together
	 */
	protected Fact combineAndStatements(List<? extends ParserRuleContext> lst) {
		if (lst == null || lst.isEmpty())
			return null;
		int size = lst.size();
		if (size == 1)
			return visit(lst.get(0));
		
		boolean initialMessageGenerationActive = this.messageGenerationActive;
		this.messageGenerationActive = false;
		List<Fact> factList = lst.stream().map(elem -> visit(elem)).collect(Collectors.toList());
		this.messageGenerationActive = initialMessageGenerationActive;
		
		Fact[] binary = new Fact[2];
		if (initialMessageGenerationActive) {
			int initialPushCounter = se.getPushCounter();
			for (int i = 0; i < size; i++) {
				int push = se.getPushCounter();
				for (int j = i + 1; j < size; j++) {
					se.pushConstraint(factList.get(j).getIdentifier());
				}
				ParserRuleContext currentCtx = lst.get(i);
				Fact currentFact = visit(currentCtx);
				se.resetConstraints(push);
//				if (currentFact.isTautologyExtended())
//					generateMessage(currentCtx, ErrorType.EXPRESSION_TAUTOLOGY);
//				else if (currentFact.isUnsatisfiableExtended())
//					generateMessage(currentCtx, ErrorType.EXPRESSION_UNSATISFIABLE);
				checkBooleanStatus(currentFact, currentCtx, StatusOrigin.EXPRESSION);
				se.pushConstraint(currentFact.getIdentifier());
				if (size == 2)
					binary[i] = currentFact;
			}
			se.resetConstraints(initialPushCounter);
		}
		
		Fact combined = factList.get(0);
		for (Fact elem: factList.subList(1, size)) {
			combined = se.binaryOperation(combined.getIdentifier(), elem.getIdentifier(), "AND");
		}
		if (size == 2) {
			boolean a = binary[0].isTautologyExtended();
			boolean b = binary[1].isTautologyExtended();
			TreeInterval interval = new TreeInterval(lst.get(0)).merge(new TreeInterval(lst.get(1)));
			if (a && !b)
				this.fb.processFeedback(interval.lineStart, interval.charStart, interval.lineStop, interval.charStop, 
						formatTree(lst.get(0)) 
						+ " AND "
						+ formatTree(lst.get(1))
						+ "\nLeft side of conjunction is tautology. Remove left expression.\n\n", Feedback.Type.MESSAGE);
			else if (!a && b)
				this.fb.processFeedback(interval.lineStart, interval.charStart, interval.lineStop, interval.charStop, 
						formatTree(lst.get(0)) 
						+ " AND "
						+ formatTree(lst.get(1))
						+ "\nRight side of conjunction is tautology. Remove right expression.\n\n", Feedback.Type.MESSAGE);
				
		}
		return combined;
	}
	
	/**
	 * Checks the status of a single expression.
	 * @param fact
	 * @param ctx Context of the ParseTree for this expression, necessary for message generation
	 */
	protected void checkStatus(Fact fact, ParserRuleContext ctx) {
		if (fact.isLiteral())
			return;
		if (fact.isJustNull()) {
			generateMessage(ctx, ErrorType.EXPRESSION_JUST_ZERO);
			return;
		}
		switch (fact.getType()) {
		case BOOL:
			checkBooleanStatus(fact, ctx, StatusOrigin.EXPRESSION);
			break;
		case INT:
			checkIntegerStatus(fact, ctx);
			break;
		default:
			break;
		}
	}

	/**
	 * Checks the status of a boolean expression.
	 * @param fact
	 * @param ctx Context of the ParseTree for this expression, necessary for message generation
	 * @param origin Where the expression appears, necessary for a more accurate message generation
	 */
	protected void checkBooleanStatus(Fact fact, ParserRuleContext ctx, StatusOrigin origin) {
		ErrorType type;
		if (fact.isTautologyExtended()) {
			switch (origin) {
			case COLUMNCONSTRAINT:
				type = ErrorType.COLUMNCONSTRAINT_TAUTOLOGY;
				break;
			case EXPRESSION:
				type = ErrorType.EXPRESSION_TAUTOLOGY;
				break;
			case EXPRESSION_CASE_WHEN:
				type = ErrorType.EXPRESSION_WHEN_TAUTOLOGY;
			default:
				return;
			}
			generateMessage(ctx, type);
		}
		else if (fact.isUnsatisfiableExtended()) {
			switch (origin) {
			case COLUMNCONSTRAINT:
				type = ErrorType.COLUMNCONSTRAINT_UNSATISFIABLE;
				break;
			case JOIN_CONSTRAINT:
				type = ErrorType.JOIN_CONSTRAINT_UNSATISFIABLE;
				break;
			case JOIN_CONSTRAINTS_COMBINED:
				type = ErrorType.JOIN_CONSTRAINTS_COMBINED_UNSATISFIABLE;
				break;
			case EXPRESSION:
				type = ErrorType.EXPRESSION_UNSATISFIABLE_OR_NULL;
				if (fact.isUnsatisfiable() && !fact.canContainNull())
					type = ErrorType.EXPRESSION_UNSATISFIABLE;
				break;
			case EXPRESSION_CASE_WHEN:
			case EXPRESSON_CASE_LAST_WHEN_NO_ELSE:
				type = ErrorType.EXPRESSION_WHEN_UNSATISFIABLE;
			default:
				return;
			}
			generateMessage(ctx, type);
		}
		else if (fact.isLeftExprUnnecessary()) {
			generateMessage(ctx, ErrorType.LEFT_EXPR_UNNECESSARY);
		}
		else if (fact.isRightExprUnnecessary()) {
			generateMessage(ctx, ErrorType.RIGHT_EXPR_UNNECESSARY);
		}
		else if (fact.isStrictEquality()) {
			generateMessage(ctx, ErrorType.EQUALITY_IS_STRICT);
		}
		else if (fact.isStrictInequality()) {
			generateMessage(ctx, ErrorType.INEQUALITY_IS_STRICT);
		}
	}

	/**
	 * Checks the status of an integer expression.
	 * @param fact
	 * @param ctx Context of the ParseTree for this expression, necessary for message generation
	 */
	protected void checkIntegerStatus(Fact fact, ParserRuleContext ctx) {
//		if (fact.isConst() && !fact.isLiteral())
//			generateMessage(ctx, ErrorType.EXPRESSION_TURNED_CONST);
		if (fact.isLeftExprUnnecessary()) {
			generateMessage(ctx, ErrorType.LEFT_EXPR_UNNECESSARY);
		}
		else if (fact.isRightExprUnnecessary()) {
			generateMessage(ctx, ErrorType.RIGHT_EXPR_UNNECESSARY);
		}
	}
	
	@Override
	public Fact visitCreate_table_stmt(SQLiteParser.Create_table_stmtContext ctx) {
		String tableName = ctx.table_name().getText();
		List<Column_defContext> columnDefs = ctx.column_def();
		if (columnDefs != null && !columnDefs.isEmpty()) {
			TableEntry[] entries = new TableEntry[columnDefs.size()];
			int i = 0;
			for (Column_defContext columnDef : columnDefs) {
				entries[i++] = processColumnDef(columnDef, tableName);
			}
			Table table = new Table(entries, tableName);
			tables.put(tableName, table);
			
			List<Table_constraintContext> tableConstraints = ctx.table_constraint();
			if (tableConstraints != null && !tableConstraints.isEmpty()) {
				int push = loadTableAlias(Arrays.asList(new Pair<String, String>(table.getName(), null)));
				Fact constraintFact = combineAndStatements(tableConstraints);
				clearTableAlias(push);
				table.setTableConstraints(constraintFact.getIdentifier());
				// TODO: maybe want to process constraintFact?
			}
		}
		// TODO: table stmt with query
		return null;
	}
	
	@Override
	public Fact visitTable_constraint(SQLiteParser.Table_constraintContext ctx) {
		// Currently only constraints with expressions
		if (ctx.CHECK() != null)
			return visit(ctx.expr());
		return null;
	}

	/**
	 * Processes a single column entry.
	 * If there is a column constraint, a check will be performed to determine whether the constraint is a tautology or unsatisfiable.
	 * @param ctx Column ParseTree context
	 * @param tableName Name of the table this column belongs to
	 * @return Table entry for this column
	 */
	protected TableEntry processColumnDef(SQLiteParser.Column_defContext ctx, String tableName) {
		String columnName = ctx.column_name().getText();
		String ref;
		String constraintRef = null;
		
		// Note: Currently ignoring Types - treating everything as an Integer.
		ref = se.createInteger().getIdentifier();
		
		List<? extends ParserRuleContext> constraints = ctx.column_constraint();
		if (!constraints.isEmpty()) {
//			scopeAlias.put(columnName, ref);
			this.tablesInScope.setEntry(columnName, ref);
			Fact result = combineAndStatements(constraints);
			checkBooleanStatus(result, ctx, StatusOrigin.COLUMNCONSTRAINT);
//			scopeAlias.clear();
			this.tablesInScope.clear();
			constraintRef = result.getIdentifier();
		}
		return new TableEntry(columnName, ref, constraintRef);
	}
	
	@Override
	public Fact visitColumn_constraint(SQLiteParser.Column_constraintContext ctx) {
		// Currently just check constraints
		if (ctx.CHECK() != null)
			return visit(ctx.expr());
		return null;
	}
	
	@Override
	public Fact visitSelect_or_values(SQLiteParser.Select_or_valuesContext ctx) {
		if (ctx.SELECT() != null) {	// SELECT statement
			List<Pair<String, String>> tablesToLoad = new ArrayList<Pair<String, String>>();
			int pushcounter = se.getPushCounter();
			// TODO invocate used tables, toplevel and other elements
			if (ctx.FROM() != null) {
				if (ctx.table_or_subquery() != null) {
					ctx.table_or_subquery().stream().forEach(entry -> processTableOrSubquery(entry, tablesToLoad));
				}
				else if (ctx.join_clause() != null) {
					processJoinClause(ctx.join_clause(), tablesToLoad);
				}
				loadTableAlias(tablesToLoad);
			}
			
			// Where clause
			if (ctx.WHERE() != null) {
				ExprContext expr = ctx.expr(0);
				Fact condition = visit(expr);
				checkBooleanStatus(condition, expr, StatusOrigin.WHERE_CONDITION);
				if (condition.isTautologyExtended())
					generateMessage(expr, ErrorType.WHERE_TAUTOLOGY, ctx.WHERE());
				else if (condition.isUnsatisfiableExtended())
					generateMessage(expr, ErrorType.WHERE_UNSATISFIABLE, ctx.WHERE());
			}
			
			// TODO: Result columns
			
			se.resetConstraints(pushcounter);
			return null;
		}
		else {	// VALUES statement
			// TODO
			return null;
		}
	}
	
	/**
	 * Processes tables or subqueries.
	 * @param ctx ParseTree context
	 * @param tablesToLoad Inserts the tables to load into this list
	 */
	protected void processTableOrSubquery(SQLiteParser.Table_or_subqueryContext ctx, List<Pair<String, String>> tablesToLoad) {
		if (ctx.table_name() != null) {
			String tableName, alias = null;
			tableName = ctx.table_name().getText();
			if (ctx.table_alias() != null)
				alias = ctx.table_alias().getText();
			tablesToLoad.add(new Pair<String, String>(tableName, alias));
		}
		else if (ctx.table_or_subquery() != null) {
			// TODO: Currently out of scope
		}
		else if (ctx.join_clause() != null) {
			processJoinClause(ctx.join_clause(), tablesToLoad);
		}
		else {
			// TODO: Subquery
		}
		
	}
	
	/**
	 * Processes joins.
	 * @param ctx ParseTree context of the join
	 * @param tablesToLoad Inserts the tables to load into this list
	 */
	protected void processJoinClause(SQLiteParser.Join_clauseContext ctx, List<Pair<String, String>> tablesToLoad) {
		// TODO include join alias and other joins, also possibly flawed logic
		List<Table_or_subqueryContext> tables = ctx.table_or_subquery();
		if (tables.size() == 1) {
			processTableOrSubquery(tables.get(0), tablesToLoad);
			return;
		}
		List<Join_constraintContext> joinConstraints = ctx.join_constraint();
		List<Pair<String, String>> joinTablesToLoad = new ArrayList<Pair<String, String>>();
		for (Table_or_subqueryContext table: tables) {
			processTableOrSubquery(table, joinTablesToLoad);
		}

		this.tablesInScope = new TableScope(this.tablesInScope);
		int pushCounter = loadTableAlias(joinTablesToLoad);
		List<ExprContext> joinConstraintExpressions = new ArrayList<ExprContext>();
		for (Join_constraintContext constraint : joinConstraints) {
			if (constraint.expr() != null) {
				ExprContext expr = constraint.expr();
				Fact exprFact = visit(expr);
				this.checkBooleanStatus(exprFact, expr, StatusOrigin.JOIN_CONSTRAINT);
				joinConstraintExpressions.add(expr);
			}
			else {
				// TODO: USING statement
			}
		}
		
		Fact allJoinsCombined = combineAndStatements(joinConstraintExpressions);
		checkBooleanStatus(allJoinsCombined, ctx, StatusOrigin.JOIN_CONSTRAINTS_COMBINED);

		// Cleanup
		clearTableAlias(pushCounter);
		tablesToLoad.addAll(joinTablesToLoad);
		se.pushConstraint(allJoinsCombined.getIdentifier());
	}
	
	@Override
	public Fact visitUpdate_stmt(SQLiteParser.Update_stmtContext ctx) {
		String tableName = ctx.qualified_table_name().table_name().getText();
		loadTableAlias(Collections.singletonList(new Pair<String, String>(tableName, null)));
		int exprCount = ctx.expr().size();
		boolean tableNoConstraints = false;
		
		Table invTable = this.tables.get(tableName).copyTable(se);
		String constraint = null;
		TableEntry[] entries = invTable.getEntries();
		Map<String, String> updatedRows = new HashMap<String, String>(entries.length);
		for (TableEntry entry : entries) {
			updatedRows.put(entry.getColumnName(), entry.getRef());
			if (entry.hasConstraint()) {
				if (constraint == null)
					constraint = entry.getConstraintRef();
				else
					constraint = se.binaryOperation(constraint, entry.getConstraintRef(), "AND").getIdentifier();
			}
		}
		if (invTable.hasTableConstraint()) {
			if (constraint == null)
				constraint = invTable.getConstraint();
			else
				constraint = se.binaryOperation(constraint, invTable.getConstraint(), "AND").getIdentifier();
		}
		if (constraint == null) {
			tableNoConstraints = true;			
		}
		else {
			Fact invConstraint = se.unaryOperation(constraint, "NOT");
			if (invConstraint.isUnsatisfiableExtended())
				tableNoConstraints = true;
			else
				se.pushConstraint(invConstraint.getIdentifier());			
		}
		
		if (ctx.WHERE() != null) {
			ExprContext whereCond = ctx.expr(exprCount - 1);
			Fact whereFact = visit(whereCond);
			checkBooleanStatus(whereFact, whereCond, StatusOrigin.WHERE_CONDITION);
			if (whereFact.isTautologyExtended())
				generateMessage(whereCond, ErrorType.WHERE_TAUTOLOGY, ctx.WHERE());
			else if (whereFact.isUnsatisfiableExtended()) {
				generateMessage(whereCond, ErrorType.WHERE_UNSATISFIABLE, ctx.WHERE());
				return null;
			}
			se.pushConstraint(whereFact.getIdentifier());
			exprCount -= 1;
		}
		
		// TPDP: combine all sets at the same time
		for (int i = 0; i < exprCount; i++) {
			String column = ctx.column_name(i).getText();
			ExprContext expr = ctx.expr(i);
			Fact exprFact = visit(expr);
			checkStatus(exprFact, expr);
			
			if (!tableNoConstraints) {
				Fact equality = se.binaryOperation(updatedRows.get(column), exprFact.getIdentifier(), "IS");
				if (!equality.isUnsatisfiable()) {
					if (i > 0)
						generateMessage(expr, ErrorType.UPDATE_ASSIGNMENT_NOT_IN_RANGE, ctx.column_name(i));
					else
						generateMessage(expr, ErrorType.UPDATE_ASSIGNMENT_NOT_IN_RANGE, ctx.column_name(i), ctx.SET());
					return null;
				}				
			}
		}

		return null;
	}
	
	@Override
	public Fact visitExpr_brace(SQLiteParser.Expr_braceContext ctx) {
		return visit(ctx.expr());
	}
	
	@Override
	public Fact visitLiteral_value(SQLiteParser.Literal_valueContext ctx) {
		if (ctx.NUMERIC_LITERAL() != null) {
			return se.nummericLiteral(ctx.NUMERIC_LITERAL().getText());
		}
		if (ctx.NULL() != null) {
			return se.createNullLiteral();
		}
		
		throw new ParserElementNotImplemented(ctx.getText());
	}
	
	@Override
	public Fact visitExpr_column_name(SQLiteParser.Expr_column_nameContext ctx) {
		String name, ref;
		// TODO: Error handling, maybe extern name combination
		if (ctx.table_name() != null) 
			name = ctx.table_name().getText() + "." + ctx.column_name().getText();
		else 
			name = ctx.column_name().getText();
//		ref = scopeAlias.get(name);
//		if (ref == null)
//			throw new RuntimeException(String.format("Name %s not found in scope. Maybe an alias error?", name));
		ref = this.tablesInScope.getEntry(name);
		
		return new Fact(ref, Fact.OK);
	}
	
	@Override
	public Fact visitExpr_unary(SQLiteParser.Expr_unaryContext ctx) {
		String symbol = ctx.unary_operator().getText();
		Fact expr = visit(ctx.expr());
		if (symbol.equals("+"))
			return expr;
		if (expr.isConstZero() && symbol.equals("-") && !expr.canContainNull())
			generateMessage(ctx, ErrorType.EXPRESSION_MINUS_ZERO);
		// TODO: Further feedback gen? Might also postbone
		
		return se.unaryOperation(expr.getIdentifier(), symbol);
	}
	
	@Override
	public Fact visitExpr_binary(SQLiteParser.Expr_binaryContext ctx) {
		String symbol;
		if (ctx.getChildCount() == 3) {
			symbol = ctx.getChild(1).getText();
		}
		else {
			symbol = ctx.getChild(1).getText() + " " + ctx.getChild(2).getText();
		}
		
		// Special case for "AND";
		// Both sides have to be valid at the same time
		if (symbol.toUpperCase().equals("AND")) {
			return combineAndStatements(ctx.expr());
		}
		
		Fact expr_left = visit(ctx.expr(0));
		Fact expr_right = visit(ctx.expr(1));
		Fact resultExpr;
		
		resultExpr = se.binaryOperation(expr_left.getIdentifier(), expr_right.getIdentifier(), symbol);
		
		// Feedback generation
		checkStatus(expr_left, ctx.expr(0));
		checkStatus(expr_right, ctx.expr(1));
		if (resultExpr.isLiteral() && !(expr_left.isConstOrLiteral() || expr_left.isConstOrLiteral()))
			generateMessage(ctx, ErrorType.EXPRESSION_TURNED_CONST);
		
		return resultExpr;
	}
	
	@Override 
	public Fact visitExpr_nullcheck(SQLiteParser.Expr_nullcheckContext ctx) {
		Fact expr = visit(ctx.expr());
		// TODO check expr?
		Fact resultExpr;
		String operand = (ctx.ISNULL() != null) ? "ISNULL" : "NOTNULL";
		resultExpr = se.unaryOperation(expr.getIdentifier(), operand);
		return resultExpr;
	}
	
	@Override
	public Fact visitExpr_case(SQLiteParser.Expr_caseContext ctx) {
		boolean exprBase = false;
		boolean elseStatementContained = false;
		if (ctx.getChild(1) instanceof SQLiteParser.ExprContext)
			exprBase = true;
		if (ctx.ELSE() != null)
			elseStatementContained = true;
		String compareTo = null;
		int exprContextSize = ctx.expr().size();
		ExprContext elseContext = (elseStatementContained) ? ctx.expr(exprContextSize - 1) : null;
		int caseStartIndex = (exprBase) ? 1 : 0;
		int caseEndIndex = (elseStatementContained) ? exprContextSize - 1 : exprContextSize;
		ListIterator<ExprContext> caseIter = ctx.expr().subList(caseStartIndex, caseEndIndex).listIterator();
		
		if (exprBase) {
			ExprContext baseContext = ctx.expr(0);
			Fact compareToFact = visit(baseContext);
			checkStatus(compareToFact, baseContext);
			compareTo = compareToFact.getIdentifier();
		}
		
		int pushCounter = se.getPushCounter();
		boolean tautologyEncountered = false;
		ArrayList<String> listWhen = new ArrayList<String>(exprContextSize / 2);
		ArrayList<String> listThen = new ArrayList<String>(exprContextSize / 2);
		while (caseIter.hasNext()) {
			ExprContext caseGuardContext = caseIter.next();
			ExprContext thenContext = caseIter.next();
			boolean lastRound = !caseIter.hasNext();
			Fact caseGuard = visit(caseGuardContext);
			Fact thenExpr;
			if (exprBase)
				caseGuard = se.binaryOperation(compareTo, caseGuard.getIdentifier(), "==");
			StatusOrigin whenOrigin = 
				(lastRound && !elseStatementContained) ? StatusOrigin.EXPRESSON_CASE_LAST_WHEN_NO_ELSE : StatusOrigin.EXPRESSION_CASE_WHEN;
			checkBooleanStatus(caseGuard, caseGuardContext, whenOrigin);
			int pushTemp = se.pushConstraint(caseGuard.getIdentifier());
			thenExpr = visit(thenContext);
			se.resetConstraints(pushTemp);
			listWhen.add(caseGuard.getIdentifier());
			listThen.add(thenExpr.getIdentifier());
			if (caseGuard.isTautologyExtended()) {
				tautologyEncountered = true;
				break;
			}
			se.pushConstraint(se.unaryOperation(caseGuard.getIdentifier(), "NOT").getIdentifier());
		}
		String elseRef = null;
		if (elseStatementContained && !tautologyEncountered)
			elseRef = visit(elseContext).getIdentifier();
		se.resetConstraints(pushCounter);
		
		return se.caseOperation(listWhen, listThen, elseRef);
	}
	
	@Override
	public Fact visitExpr_between(SQLiteParser.Expr_betweenContext ctx) {
		Fact exprBetween = visit(ctx.expr(0));
		Fact exprLeft = visit(ctx.expr(1));
		Fact exprRight = visit(ctx.expr(2));
		String comparisonLeft, comparisonRight, connection;
		
		if (ctx.NOT() != null) {
			comparisonLeft = "<";
			comparisonRight = ">";
			connection = "OR";
		}
		else {
			comparisonLeft = ">=";
			comparisonRight = "<=";
			connection = "AND";
		}
		
		Fact factLeft = se.binaryOperation(exprBetween.getIdentifier(), exprLeft.getIdentifier(), comparisonLeft);
		Fact factRight = se.binaryOperation(exprBetween.getIdentifier(), exprRight.getIdentifier(), comparisonRight);
		boolean leftUnsatisfiable = factLeft.isUnsatisfiableExtended();
		boolean leftTautology = factLeft.isTautologyExtended();
		boolean rightUnsatisfiable = factRight.isUnsatisfiableExtended();
		boolean rightTautology = factRight.isTautologyExtended();
		
		if (leftUnsatisfiable || leftTautology || rightUnsatisfiable || rightTautology) {
			// TODO: Refactor
			TreeInterval interval = new TreeInterval(ctx);
			String message = "";
			boolean notContained = ctx.NOT() != null;
			String not = notContained ? "NOT " : "";
			String template = "At expression: " + formatTree(ctx) + "\n"
				+ "In \"x " + not + "BETWEEN a AND b: %s";
			boolean leftOK = !(leftUnsatisfiable || leftTautology), rightOK = !(rightUnsatisfiable || rightTautology);
			
			if (leftUnsatisfiable)
				message = String.format("\"%sx >= a\" is unsatisfiable. %s\n", not, (notContained && rightOK ) ? "Change to \"x >= b\"." : "");
			else if (leftTautology)
				message = String.format("\"%sx >= a\" is tautology. %s\n", not, (!notContained && rightOK) ? "Change to \"x <= b\"." : "");
			if (rightUnsatisfiable)
				message += String.format("\"%sx <= b\" is unsatisfiable. %s\n", not, (notContained && leftOK) ? "Change to \"x <= a\"." : "");
			else if (rightTautology)
				message += String.format("\"%sx <= b\" is tautology. %s\n", not, (!notContained && leftOK) ? "Change to \"x >= a\"." : "");
			
			fb.processFeedback(interval.lineStart, interval.charStart, interval.lineStop, interval.charStop, String.format(template, message), Type.MESSAGE);
		}
		
		return se.binaryOperation(factLeft.getIdentifier(), factRight.getIdentifier(), connection);
	}
	
	public Fact visitValues_expr(SQLiteParser.Values_exprContext ctx) {
		List<ExprContext> expr = ctx.expr();
		ArrayList<String> refs = new ArrayList<String>(expr.size());
		for (ExprContext singleExpr : expr) {
			Fact result = visit(singleExpr);
			checkStatus(result, singleExpr);
			refs.add(result.getIdentifier());
		}
		return se.createRowValue(refs);
	}
	
	/**
	 * Converts a ParseTree context to a String.
	 * @param ctx A ParseTree
	 * @return Trimmed String of the ParseTree
	 */
	protected String formatTree(ParseTree ctx) {
		return formatTree(ctx, false);
	}
	
	/**
	 * Converts a ParseTree context to a String.
	 * @param ctx A ParseTree
	 * @param printLineBreaks Include line breaks (currently not implemented)
	 * @return Trimmed String of the ParseTree
	 */
	protected String formatTree(ParseTree ctx, boolean printLineBreaks) {
		StringBuilder sb = new StringBuilder();
		formatTree(ctx, sb, printLineBreaks);
		return sb.toString().trim();
	}
	
	private void formatTree(ParseTree ctx, StringBuilder sb, boolean printLineBreaks) {
		int childCount = ctx.getChildCount();
		if (childCount == 0) {
			// TODO handle '(' and ')'
			sb.append(ctx.getText());
			sb.append(' ');
		}
		else {
			// TODO implement line breaks
			for (int i = 0; i < childCount; i++) {
				formatTree(ctx.getChild(i), sb, printLineBreaks);
			}
		}
	}
	
	protected enum ErrorType {
		COLUMNCONSTRAINT_TAUTOLOGY,
		COLUMNCONSTRAINT_UNSATISFIABLE,
		JOIN_CONSTRAINT_UNSATISFIABLE,
		JOIN_CONSTRAINTS_COMBINED_UNSATISFIABLE,
		WHERE_TAUTOLOGY,
		WHERE_UNSATISFIABLE,
		EXPRESSION_TAUTOLOGY,
		EXPRESSION_UNSATISFIABLE, 
		EXPRESSION_WHEN_TAUTOLOGY, 
		EXPRESSION_WHEN_UNSATISFIABLE,
		EXPRESSION_UNSATISFIABLE_OR_NULL, 
		EXPRESSION_MINUS_ZERO, 
		EXPRESSION_JUST_ZERO, 
		EXPRESSION_TURNED_CONST, 
		UPDATE_ASSIGNMENT_NOT_IN_RANGE, 
		LEFT_EXPR_UNNECESSARY, 
		RIGHT_EXPR_UNNECESSARY, 
		EQUALITY_IS_STRICT, 
		INEQUALITY_IS_STRICT, 
	}
	
	protected enum StatusOrigin {
		COLUMNCONSTRAINT,
		WHERE_CONDITION,
		JOIN_CONSTRAINT,
		JOIN_CONSTRAINTS_COMBINED,
		EXPRESSION,
		EXPRESSION_CASE_WHEN, 
		EXPRESSON_CASE_LAST_WHEN_NO_ELSE, 
	}
	
	/**
	 * TreeInterval is a container storing positional information of a parsetree.
	 */
	protected class TreeInterval {
		public final int lineStart, lineStop, charStart, charStop;
		
		public TreeInterval(ParserRuleContext ctx) {
			Token first = ctx.getStart();
			Token last = ctx.getStop();
			this.lineStart = first.getLine();
			this.lineStop = last.getLine();
			this.charStart = first.getCharPositionInLine();
			this.charStop = last.getCharPositionInLine() + last.getText().length() - 1;
		}
		
		private TreeInterval(int lstart, int lstop, int cstart, int cstop) {
			this.lineStart = lstart;
			this.lineStop = lstop;
			this.charStart = cstart;
			this.charStop = cstop;
		}
		
		public TreeInterval(TerminalNode node) {
			int line = node.getSymbol().getLine();
			int charStart = node.getSymbol().getCharPositionInLine();
			this.lineStart = line;
			this.lineStop = line;
			this.charStart = charStart;
			this.charStop = charStart + node.getText().length() - 1;
		}

		public TreeInterval merge(TreeInterval other) {
			int lstart = Math.min(this.lineStart, other.lineStart);
			int lstop = Math.max(this.lineStop, other.lineStop);
			int cstart = Math.min(this.charStart, other.charStart);
			int cstop = Math.max(this.charStop, other.charStop);
			return new TreeInterval(lstart, lstop, cstart, cstop);
		}
	}
	
	/**
	 * Wrapper for a table column.
	 */
	protected class TableEntry {
		private String name, ref, constraintRef;
		
		protected TableEntry(String columnName, String reference) {
			this(columnName, reference, null);
		}
		
		protected TableEntry(String columnName, String reference, String constraintReference) {
			this.name = columnName;
			this.ref = reference;
			this.constraintRef = constraintReference;
		}
		
		protected String getColumnName() { return name; }
		protected String getRef() { return ref; }
		protected String getConstraintRef() { return constraintRef; }
		protected boolean hasConstraint() {
			return this.constraintRef != null;
		}
	}
	
	/**
	 * Wrapper for a table.
	 */
	protected class Table {
		private String tableName;
		private TableEntry[] entries;
		private String tableConstraintRef;
		
		protected Table(TableEntry[] tableEntryList, String tableName, String tableConstraintRef) {
			this.tableName = tableName;
			this.entries = tableEntryList;
			this.tableConstraintRef = tableConstraintRef;
		}
		protected Table(TableEntry[] tableEntryList, String tableName) {
			this(tableEntryList, tableName, null);
		}
		
		protected void setTableConstraints(String ref) { this.tableConstraintRef = ref; }
		protected String getName() { return this.tableName; }
		protected TableEntry[] getEntries() { return this.entries; }
		protected String getConstraint() { return this.tableConstraintRef; }
		protected boolean hasTableConstraint() {
			return this.tableConstraintRef != null;
		}
		protected Table copyTable(SymbolicExecution se) {
			ArrayList<String> refs = new ArrayList<String>(2 * this.entries.length + 1);
			for (TableEntry entry : this.entries) {
				refs.add(entry.getRef());
				String constraint = entry.getConstraintRef();
				if (constraint != null)
					refs.add(constraint);
			}
			if (this.hasTableConstraint())
				refs.add(this.tableConstraintRef);
			String[] newRefs = se.createReferenceCopies(refs.toArray(new String[0]));
			TableEntry[] entryCopy = new TableEntry[this.entries.length];
			int refIndex = 0;
			for (int i = 0; i < entryCopy.length; i++) {
				String name, ref, constraint = null;
				TableEntry entry = this.entries[i];
				name = entry.getColumnName();
				ref = newRefs[refIndex++];
				if (entry.hasConstraint())
					constraint = newRefs[refIndex++];
				entryCopy[i] = new TableEntry(name, ref, constraint);
			}
			String tableConstraint = (this.hasTableConstraint()) ? newRefs[refIndex] : null;
			return new Table(entryCopy, this.tableName, tableConstraint);
		}
	}
	
	/**
	 * Stores current tables in scope.
	 */
	protected class TableScope {
		private TableScope previous;
		private TreeMap<String, String> tableReferences;
		private TreeSet<String> tablesUsed;
		private int index;
		
		protected TableScope(TreeMap<String, String> tableReferences, TableScope previousTableScope) {
			this.previous = previousTableScope;
			this.tableReferences = tableReferences;
			if (previousTableScope != null) {
				this.tablesUsed = previousTableScope.copyUsedTables();
				this.index = previousTableScope.getIndex() + 1;
			}
			else {
				this.tablesUsed = new TreeSet<String>();
				this.index = 0;
			}
		}
		
		protected TableScope(TableScope previousTableScope) {
			this(
				(previousTableScope == null) ? new TreeMap<String, String>() : previousTableScope.copyTableReferences(), 
				previousTableScope
			);
		}
		
		protected int getIndex() { return this.index; }
		protected TableScope getPrevious() { return this.previous; }
		
		protected TableScope clearAndGetPrevious() {
			this.clear();
			return this.previous;
		}
		
		protected void clear() {
			this.tableReferences.clear();
			this.tablesUsed.clear();
		}
		
		@SuppressWarnings("unchecked")
		protected TreeMap<String, String> copyTableReferences() {
			return (TreeMap<String, String>) this.tableReferences.clone();
		}
		
		@SuppressWarnings("unchecked")
		protected TreeSet<String> copyUsedTables() {
			return (TreeSet<String>) this.tablesUsed.clone();
		}
		
		protected TableScope resetUntilIndex(int index) {
			if (this.index > index)
				return this.clearAndGetPrevious().resetUntilIndex(index);
			else
				return this;
		}
		
		protected TableScope resetAll() {
			TableScope temp = this.resetUntilIndex(0);
			temp.clear();
			return temp;
		}
		
		protected String getEntry(String entryName) {
			String result = this.tableReferences.get(entryName);
			if (result == null)
				throw new RuntimeException(String.format("Name %s not found in scope. Maybe an alias error?", entryName));
			return result;
		}
		
		protected void setEntry(String entryName, String ref) {
			this.tableReferences.put(entryName, ref);
		}
		
		protected boolean isTableUsed(String tableName) {
			return this.tablesUsed.contains(tableName);
		}
		
		protected boolean insertTableUsed(String tableName) {
			return this.tablesUsed.add(tableName);
		}
		
	}
	
}
