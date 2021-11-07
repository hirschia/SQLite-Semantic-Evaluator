package symbolicexecution.sqlite.type;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;

import symbolicexecution.exceptions.ConversionException;
import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLBool;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLRowValue;

/**
 * An SQLite Boolean value, which can be NULL.
 */
public class SQLiteBool extends SQLBool {
	private final BoolExpr boolExpr;
	private final BoolExpr nullExpr;
	
	public SQLiteBool(BoolExpr boolExpr, BoolExpr nullExpr) {
		this.boolExpr = boolExpr;
		this.nullExpr = nullExpr;
	}
	
	public SQLiteBool(boolean value) {
		BoolExpr falseExpr = ctx.mkNot(ctx.mkTrue());
		this.nullExpr = falseExpr;
		this.boolExpr = (value) ? ctx.mkTrue() : falseExpr;
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType not() {
		BoolExpr result = ctx.mkNot(this.boolExpr);
		return new SQLiteBool(result, this.nullExpr);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseNot() {
		return this.convertToIntegerType().bitwiseNot();
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType negation() {
		return this.convertToIntegerType().negation();
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType isNull() {
		return new SQLiteBool(this.nullExpr, ctx.mkNot(ctx.mkBool(true)));
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType isNotNull() {
		return new SQLiteBool(ctx.mkNot(this.nullExpr), ctx.mkNot(ctx.mkBool(true)));
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType add(SQLAnyType other) {
		return this.convertToIntegerType().add(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType sub(SQLAnyType other) {
		return this.convertToIntegerType().sub(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType mul(SQLAnyType other) {
		return this.convertToIntegerType().mul(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType div(SQLAnyType other) {
		return this.convertToIntegerType().div(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType mod(SQLAnyType other) {
		return this.convertToIntegerType().mod(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType and(SQLAnyType other) {
		BoolExpr result = ctx.mkAnd(this.boolExpr, other.convertToBoolType().getBoolExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType or(SQLAnyType other) {
		SQLBool otherBool = other.convertToBoolType();
		BoolExpr result = ctx.mkOr(this.boolExpr, otherBool.getBoolExpr());
		BoolExpr leftNotTrue = ctx.mkOr(this.nullExpr, ctx.mkNot(this.boolExpr));
		BoolExpr rightNotTrue = ctx.mkOr(otherBool.getNullExpr(), ctx.mkNot(otherBool.getBoolExpr()));
		BoolExpr neitherTrue = ctx.mkAnd(leftNotTrue, rightNotTrue);
		BoolExpr resultNull = ctx.mkAnd(ctx.mkOr(this.nullExpr, otherBool.getNullExpr()), neitherTrue);
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType eq(SQLAnyType other) {
		if (other.getType() == Type.BOOL) {
			BoolExpr result = ctx.mkEq(this.boolExpr, other.convertToBoolType().getBoolExpr());
			BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
			return new SQLiteBool(result, resultNull);
		}
		return this.convertToIntegerType().eq(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType neq(SQLAnyType other) {
		if (other.getType() == Type.BOOL) {
			SQLBool otherBool = other.convertToBoolType();
			BoolExpr result = ctx.mkNot(ctx.mkEq(this.boolExpr, otherBool.getBoolExpr()));
			BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
			return new SQLiteBool(result, resultNull);
		}
		return this.convertToIntegerType().neq(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType gt(SQLAnyType other) {
		return this.convertToIntegerType().gt(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType geq(SQLAnyType other) {
		return this.convertToIntegerType().geq(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lt(SQLAnyType other) {
		return this.convertToIntegerType().lt(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType leq(SQLAnyType other) {
		return this.convertToIntegerType().leq(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lls(SQLAnyType other) {
		return this.convertToIntegerType().lls(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lrs(SQLAnyType other) {
		return this.convertToIntegerType().lrs(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseAnd(SQLAnyType other) {
		return this.convertToIntegerType().bitwiseAnd(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseOr(SQLAnyType other) {
		return this.convertToIntegerType().bitwiseOr(other);
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType is(SQLAnyType other) {
		if (other.getType() != Type.BOOL)
			return this.convertToIntegerType().is(other);
		
		BoolExpr comparison = ctx.mkEq(this.boolExpr, other.convertToBoolType().getBoolExpr());
		BoolExpr implication = ctx.mkImplies(ctx.mkNot(ctx.mkOr(this.nullExpr, other.getNullExpr())), comparison);
		BoolExpr bothNull = ctx.mkAnd(this.nullExpr, other.getNullExpr());
		BoolExpr result = ctx.mkOr(implication, bothNull);
		BoolExpr resultNull = ctx.mkNot(ctx.mkBool(true));
		return new SQLiteBool(result, resultNull);
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType isNot(SQLAnyType other) {
		SQLAnyType isStep = this.is(other);
		return isStep.not();
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType iif(SQLAnyType thenExpr, SQLAnyType elseExpr) {
		// Note: The current implementation only considers Integers in SQLite.
		// If more Datatypes are to be implemented, then there would arise a need
		// to implement the SQLite datatypes as algebraic datatypes (or possibly function defs)
		// in Z3 for this function to mix return types.
		// On the other hand, if SQLite datatypes are properly implemented as Z3 algebraic types,
		// then one can refactor and get rid of the sqltype classes.
		Type t1 = thenExpr.getType(), t2 = elseExpr.getType();
		if (t1 != t2) {
			if ((t1 == Type.BOOL || t1 == Type.INTEGER) && (t2 == Type.BOOL || t2 == Type.INTEGER))
				return this.iif(thenExpr.convertToIntegerType(), elseExpr.convertToIntegerType());
			else
				throw new ConversionException("Type missmatch in 'iif'.\n");
		}
		else {
			BoolExpr condition = ctx.mkAnd(this.boolExpr, ctx.mkNot(this.nullExpr));
			BoolExpr nullExpr = (BoolExpr) ctx.mkITE(condition, thenExpr.getNullExpr(), elseExpr.getNullExpr());
			switch (t1) {
			case BOOL:
				SQLiteBool bool1 = (SQLiteBool) thenExpr, bool2 = (SQLiteBool) elseExpr;
				BoolExpr boolResult = (BoolExpr) ctx.mkITE(condition, bool1.getBoolExpr(), bool2.getBoolExpr());
				return new SQLiteBool(boolResult, nullExpr);
			case INTEGER:
				SQLiteInteger int1 = (SQLiteInteger) thenExpr, int2 = (SQLiteInteger) elseExpr;
				IntExpr intResult = (IntExpr) ctx.mkITE(condition, int1.getIntExpr(), int2.getIntExpr());
				return new SQLiteInteger(intResult, nullExpr);
			default:
				throw new ConversionException("Unimplemented type in 'iff'.\n");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType in(SQLAnyType other) {
		if (other.getType() != Type.ROW_VALUE)
			throw new ConversionException("Right side of 'IN' operation not a set.\n");
		SQLRowValue set = (SQLRowValue) other;
		if (set.getRowSize() == 0)
			return new SQLiteBool(false);
		if (set.getRowSize() > 1)
			throw new ConversionException("Right side of 'IN' operation has wrong number of columns.\n");
		
		SQLiteBool intermediateResult = (SQLiteBool) this.eq(set.getRowContent()[0]);
		BoolExpr cmp = intermediateResult.getBoolExpr();
		BoolExpr nullExpr = intermediateResult.getNullExpr();
		
		BoolExpr resultNullExpr = ctx.mkOr(this.nullExpr, ctx.mkAnd(ctx.mkNot(cmp), nullExpr));
		return new SQLiteBool(cmp, resultNullExpr);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType notIn(SQLAnyType other) {
		return this.in(other).not();
	}

	/** {@inheritDoc} */
	@Override
	public SQLBool convertToBoolType() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public SQLInteger convertToIntegerType() {
		IntExpr intExpr = (IntExpr) ctx.mkITE(this.boolExpr, ctx.mkInt(1), ctx.mkInt(0));
		return new SQLiteInteger(intExpr, this.nullExpr);
	}

	/** {@inheritDoc} */
	@Override
	public BoolExpr getBoolExpr() {
		return this.boolExpr;
	}
	
	/** {@inheritDoc} */
	@Override
	public BoolExpr getNullExpr() {
		return this.nullExpr;
	}

	/** {@inheritDoc} */
	@Override
	public Expr[] getAllExpr() {
		return new Expr[] {this.boolExpr, this.nullExpr};
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType substitute(Expr[] from, Expr[] to) {
		BoolExpr boolExpr = (BoolExpr) this.boolExpr.substitute(from, to);
		BoolExpr nullExpr = (BoolExpr) this.nullExpr.substitute(from, to);
		return new SQLiteBool(boolExpr, nullExpr);
	}
	
}
