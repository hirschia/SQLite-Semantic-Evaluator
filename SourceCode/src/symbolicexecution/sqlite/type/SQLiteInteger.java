package symbolicexecution.sqlite.type;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;

import symbolicexecution.exceptions.ConversionException;
import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLBool;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLRowValue;
import symbolicexecution.sqltype.SQLAnyType.Type;

/**
 * An SQLite Integer.
 */
public class SQLiteInteger extends SQLInteger {
	private final IntExpr intExpr;
	private final BoolExpr nullExpr;
	
	public SQLiteInteger(String name, String nullName) {
		this.intExpr = ctx.mkIntConst(name);
		this.nullExpr = ctx.mkBoolConst(nullName);
	}
	
	public SQLiteInteger(long literalValue) {
		this.intExpr = ctx.mkInt(literalValue);
		// Segmentation fault in next line if solver checks for (not false) (probably a z3 bug), thus workaround:
//		this.nullExpr = ctx.mkBool(false);
		this.nullExpr = ctx.mkNot(ctx.mkBool(true));
	}
	
	public SQLiteInteger(IntExpr intExpr, BoolExpr nullExpr) {
		this.intExpr = intExpr;
		this.nullExpr = nullExpr;
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType not() {
		return this.convertToBoolType().not();
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseNot() {
		BitVecExpr bv = ctx.mkInt2BV(64, this.intExpr);
		BitVecExpr bv_inverted = ctx.mkBVNot(bv);
		IntExpr result = ctx.mkBV2Int(bv_inverted, true);
		return new SQLiteInteger(result, this.nullExpr);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType negation() {
		IntExpr result = (IntExpr) ctx.mkSub(ctx.mkInt(0), this.intExpr);
		return new SQLiteInteger(result, this.nullExpr);
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
		SQLInteger otherInt = other.convertToIntegerType();
		IntExpr result = (IntExpr) ctx.mkAdd(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType sub(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		IntExpr result = (IntExpr) ctx.mkSub(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType mul(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		IntExpr result = (IntExpr) ctx.mkMul(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType div(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BitVecExpr bv1 = ctx.mkInt2BV(64, this.intExpr);
		BitVecExpr bv2 = ctx.mkInt2BV(64, otherInt.getIntExpr());
		BitVecExpr divResult = ctx.mkBVSDiv(bv1, bv2);
//		IntExpr result = (IntExpr) ctx.mkDiv(this.intExpr, otherInt.getIntExpr());
		IntExpr result = ctx.mkBV2Int(divResult, true);
		// Division by 0 results in NULL
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr(), ctx.mkEq(otherInt.getIntExpr(), ctx.mkInt(0)));
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType mod(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		// TODO: Implement correct modulo behavior if left expr is negative
		IntExpr result = (IntExpr) ctx.mkMod(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType and(SQLAnyType other) {
		return this.convertToBoolType().and(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType or(SQLAnyType other) {
		return this.convertToBoolType().and(other);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType eq(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkEq(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType neq(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkNot(ctx.mkEq(this.intExpr, otherInt.getIntExpr()));
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType gt(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkGt(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType geq(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkGe(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lt(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkLt(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType leq(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr result = ctx.mkLe(this.intExpr, otherInt.getIntExpr());
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteBool(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lls(SQLAnyType other) {
		IntExpr otherIntExpr = other.convertToIntegerType().getIntExpr();
		IntExpr shiftedExpr = (IntExpr) ctx.mkMul(this.intExpr, ctx.mkPower(ctx.mkInt(2), otherIntExpr));
		BoolExpr positiveCheck = ctx.mkGe(otherIntExpr, ctx.mkInt(0));
		IntExpr result = (IntExpr) ctx.mkITE(positiveCheck, shiftedExpr, this.intExpr);
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType lrs(SQLAnyType other) {
		IntExpr otherIntExpr = other.convertToIntegerType().getIntExpr();
		IntExpr shiftedExpr = (IntExpr) ctx.mkDiv(this.intExpr, ctx.mkPower(ctx.mkInt(2), otherIntExpr));
		BoolExpr positiveCheck = ctx.mkGe(otherIntExpr, ctx.mkInt(0));
		IntExpr result = (IntExpr) ctx.mkITE(positiveCheck, shiftedExpr, this.intExpr);
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseAnd(SQLAnyType other) {
		BitVecExpr bv1 = ctx.mkInt2BV(64, this.intExpr);
		BitVecExpr bv2 = ctx.mkInt2BV(64, other.convertToIntegerType().getIntExpr());
		IntExpr result = ctx.mkBV2Int(ctx.mkBVAND(bv1, bv2), true);
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType bitwiseOr(SQLAnyType other) {
		BitVecExpr bv1 = ctx.mkInt2BV(64, this.intExpr);
		BitVecExpr bv2 = ctx.mkInt2BV(64, other.convertToIntegerType().getIntExpr());
		IntExpr result = ctx.mkBV2Int(ctx.mkBVOR(bv1, bv2), true);
		BoolExpr resultNull = ctx.mkOr(this.nullExpr, other.getNullExpr());
		return new SQLiteInteger(result, resultNull);
	}
	
	/** {@inheritDoc} */
	@Override
	public SQLAnyType is(SQLAnyType other) {
		SQLInteger otherInt = other.convertToIntegerType();
		BoolExpr comparison = ctx.mkEq(this.intExpr, otherInt.getIntExpr());
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
		return this.convertToBoolType().iif(thenExpr, elseExpr);
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
		BoolExpr boolExpr = ctx.mkOr(ctx.mkLt(this.intExpr, ctx.mkInt(0)), ctx.mkGt(this.intExpr, ctx.mkInt(0)));
		return new SQLiteBool(boolExpr, this.nullExpr);
	}

	/** {@inheritDoc} */
	@Override
	public SQLInteger convertToIntegerType() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public IntExpr getIntExpr() {
		return this.intExpr;
	}
	
	/** {@inheritDoc} */
	@Override
	public BoolExpr getNullExpr() {
		return this.nullExpr;
	}

	/** {@inheritDoc} */
	@Override
	public Expr[] getAllExpr() {
		return new Expr[] {this.intExpr, this.nullExpr};
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType substitute(Expr[] from, Expr[] to) {
		IntExpr intExpr = (IntExpr) this.intExpr.substitute(from, to);
		BoolExpr nullExpr = (BoolExpr) this.nullExpr.substitute(from, to);
		return new SQLiteInteger(intExpr, nullExpr);
	}
	
}
