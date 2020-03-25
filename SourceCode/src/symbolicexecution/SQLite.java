package symbolicexecution;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.sqlite.SQLiteBaseVisitor;
import parser.sqlite.SQLiteParser;
import parser.sqlite.SQLiteParser.*;
import symbolicexecution.Feedback.Type;
import symbolicexecution.exceptions.ParserElementNotImplemented;

public class SQLite extends SQLiteBaseVisitor<Fact> {
	
	protected final SymbolicExecution se;
	protected final Feedback fb;
	protected static int TAUTOLOGY = SymbolicExecution.STATUS_NONZERO | SymbolicExecution.STATUS_TAUTOLOGY;
	protected static int UNSATISFIABLE = SymbolicExecution.STATUS_ZERO | SymbolicExecution.STATUS_UNSATISFIABLE;
	
	public SQLite(Feedback feedback) {
		this.fb = feedback;
		this.se = new SymbolicExecution();
	}
	
	
	@Override
	public Fact visitSelect_core(SQLiteParser.Select_coreContext ctx) {
		// TODO invocate used tables, toplevel and other elements
		
		// Where clause
		if (ctx.WHERE() != null) {
			ExprContext expr = ctx.expr(0);
			Fact condition = visit(expr);
			
			if (condition.getStatusCode() != SymbolicExecution.STATUS_OK) {
				TreeInterval ti = new TreeInterval(expr);
				if ((condition.getStatusCode() & TAUTOLOGY) != 0) {
					// TODO Further feedback
					String message = String.format("Expression %s is tautology.", formatTree(expr));
					fb.processFeedback(ti.lineStart, ti.charStart, ti.lineStop, ti.charStop, message, Type.MESSAGE);
				}
				else if ((condition.getStatusCode() & UNSATISFIABLE) != 0) {
					// TODO Further feedback
					String message = String.format("Expression %s is unsatisfiable.", formatTree(expr));
					fb.processFeedback(ti.lineStart, ti.charStart, ti.lineStop, ti.charStop, message, Type.MESSAGE);
				}				
			}
		}
		
		return null;
	}
	
	@Override
	public Fact visitExpr_binary(SQLiteParser.Expr_binaryContext ctx) {
		Fact expr_left = visit(ctx.expr(0));
		Fact expr_right = visit(ctx.expr(1));
		
		String symbol;
		if (ctx.getChildCount() == 3) {
			symbol = ctx.getChild(1).getText();
		}
		else {
			// TODO case "is not"
			throw new ParserElementNotImplemented("");
		}
		// Feedback generation
		// TODO
		
		return se.binaryOperation(expr_left.getIdentifier(), expr_right.getIdentifier(), symbol);
	}
	
	@Override
	public Fact visitLiteral_value(SQLiteParser.Literal_valueContext ctx) {
		if (ctx.NUMERIC_LITERAL() != null) {
			return se.nummericLiteral(ctx.NUMERIC_LITERAL().getText());
		}
		
		throw new ParserElementNotImplemented(ctx.getText());
	}
	
	protected String formatTree(ParseTree ctx) {
		return formatTree(ctx, false);
	}
	
	protected String formatTree(ParseTree ctx, boolean printLineBreaks) {
		StringBuilder sb = new StringBuilder();
		formatTree(ctx, sb, printLineBreaks);
		return sb.toString();
	}
	
	private void formatTree(ParseTree ctx, StringBuilder sb, boolean printLineBreaks) {
		int childCount = ctx.getChildCount();
		if (childCount == 0) {
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
	}
	
}
