package symbolicexecution.sqlite.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;

import symbolicexecution.exceptions.ConversionException;
import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLBool;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLRowValue;
import symbolicexecution.sqltype.SQLAnyType.Type;

public class SQLiteRowValue extends SQLRowValue {
	private static int tmpCounter = 0;
	
	protected final SQLAnyType[] exprList;
	
	public SQLiteRowValue(List<SQLAnyType> elements) {
		this.exprList = elements.toArray(new SQLAnyType[0]);
	}
	
	public SQLiteRowValue(SQLAnyType[] elements) {
		this.exprList = elements;
	}

	@Override
	public SQLAnyType not() {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType bitwiseNot() {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType negation() {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType isNull() {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType isNotNull() {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType add(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType sub(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType mul(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType div(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType mod(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType and(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType or(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType eq(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType neq(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType gt(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType geq(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType lt(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType leq(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType lls(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType lrs(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType bitwiseAnd(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType bitwiseOr(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType is(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType isNot(SQLAnyType other) {
		throw new ConversionException("Illegal operation on row value.\n");
	}

	@Override
	public SQLAnyType iif(SQLAnyType thenExpr, SQLAnyType elseExpr) {
		throw new ConversionException("Illegal operation on row value.\n");
	}
	
	@Override
	public SQLAnyType in(SQLAnyType other) {
		if (this.exprList.length == 0)
			throw new ConversionException("Left side of 'IN' operation is an empty set.\n");
		if (other.getType() != Type.ROW_VALUE)
			throw new ConversionException("Right side of 'IN' operation not a set.\n");
		SQLRowValue set = (SQLRowValue) other;
		if (set.getRowSize() == 0)
			return new SQLiteBool(false);
		if (set.getRowSize() != this.getRowSize())
			throw new ConversionException("Right side of 'IN' operation has wrong number of columns.\n");
		
//		SQLiteBool intermediateResult = (SQLiteBool) this.eq(set.getRowContent()[0]);
//		BoolExpr cmp = intermediateResult.getBoolExpr();
//		BoolExpr nullExpr = intermediateResult.getNullExpr();
//		
//		BoolExpr resultNullExpr = ctx.mkOr(this.nullExpr, ctx.mkAnd(ctx.mkNot(cmp), nullExpr));
//		return new SQLiteBool(cmp, resultNullExpr);

		
		return null;
	}

	@Override
	public SQLAnyType notIn(SQLAnyType other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLRowValue setUnion(SQLAnyType other) {
		if (other.getType() != Type.ROW_VALUE)
			throw new ConversionException("Illegal operation on row value.\n");
		SQLAnyType[] otherValues = ((SQLiteRowValue) other).getRowContent();
		if (this.exprList.length != otherValues.length)
			throw new ConversionException("Trying to combine sets of different length.\n");
		BoolExpr selectionExpr = ctx.mkBoolConst("__rowvalue_combine__" + String.valueOf(tmpCounter++));
		SQLiteBool selection = new SQLiteBool(selectionExpr, ctx.mkNot(ctx.mkBool(true)));
		SQLAnyType[] result = new SQLAnyType[this.exprList.length];
		
		for (int i = 0; i < this.exprList.length; i++) {
			result[i] = selection.iif(this.exprList[i], otherValues[i]);
		}
		
		return new SQLiteRowValue(result);
	}

	@Override
	public SQLBool convertToBoolType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLInteger convertToIntegerType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoolExpr getNullExpr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expr[] getAllExpr() {
		List<Expr> expr = new ArrayList<Expr>();
		for (SQLAnyType single : this.exprList) {
			Collections.addAll(expr, single.getAllExpr());
		}
		return expr.toArray(new Expr[0]);
	}
	
	@Override
	public SQLAnyType[] getRowContent() {
		return this.exprList;
	}
	
	@Override
	public int getRowSize() {
		return this.exprList.length;
	}

	@Override
	public SQLAnyType substitute(Expr[] from, Expr[] to) {
		SQLAnyType[] result = new SQLAnyType[this.exprList.length];
		for (int i = 0; i < this.exprList.length; i++)
			result[i] = this.exprList[i].substitute(from, to);
		return new SQLiteRowValue(result);
	}

}
