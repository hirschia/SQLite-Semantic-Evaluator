package symbolicexecution.sqltype;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import org.eclipse.persistence.exceptions.ConversionException;

/**
 * Abstract class of an SQL type.
 */
public abstract class SQLAnyType {
	public enum Type {
		INTEGER,
		BOOL,
		ROW_VALUE
	}
	
	protected static Context ctx;
	
	public static void setZ3Context(Context context) {
		SQLAnyType.ctx = context;
	}
	public static Context getZ3Context() {
		return SQLAnyType.ctx;
	}
	
	/**
	 * Logical not.
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType not();
	/**
	 * Binary (bitwise) not.
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType bitwiseNot();
	/**
	 * Integer negation.
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType negation();
	/**
	 * SQL "IS NULL" operation.
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType isNull();
	/**
	 * SQL "IS NOT NULL" operation.
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType isNotNull();
	/**
	 * Binary operation: Addition.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType add(SQLAnyType other);
	/**
	 * Binary operation: Subtraction.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType sub(SQLAnyType other);
	/**
	 * Binary operation: Multiplication.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType mul(SQLAnyType other);
	/**
	 * Binary operation: Division.
	 * @param other Right side of the binary operation (i.e. the divisor)
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType div(SQLAnyType other);
	/**
	 * Binary operation: Modulo.
	 * @param other Right side of the binary operation (i.e. the modulus)
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType mod(SQLAnyType other);
	/**
	 * Binary operation: AND
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType and(SQLAnyType other);
	/**
	 * Binary operation: OR
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType or(SQLAnyType other);
	/**
	 * Binary operation: ==
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType eq(SQLAnyType other);
	/**
	 * Binary operation: !=
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType neq(SQLAnyType other);
	/**
	 * Binary operation: >
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType gt(SQLAnyType other);
	/**
	 * Binary operation: >=
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType geq(SQLAnyType other);
	/**
	 * Binary operation: <
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType lt(SQLAnyType other);
	/**
	 * Binary operation: <=
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType leq(SQLAnyType other);
	/**
	 * Binary operation: Logical left shift.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType lls(SQLAnyType other);
	/**
	 * Binary operation: Logical right shift.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType lrs(SQLAnyType other);
	/**
	 * Binary operation: Binary (bitwise) and.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType bitwiseAnd(SQLAnyType other);
	/**
	 * Binary operation: Binary (bitwise) or.
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType bitwiseOr(SQLAnyType other);
	/**
	 * Binary operation: SQL "IS".
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType is(SQLAnyType other);
	/**
	 * Binary operation: SQL "IS NOT".
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType isNot(SQLAnyType other);
	/**
	 * Ternary operation: IF {@code this} THEN {@code thenExpr} ELSE {@code else Expr}.
	 * @param thenExpr Resulting expression when {@code this} is true
	 * @param elseExpr Resulting expression when {@code this} is false or NULL
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType iif(SQLAnyType thenExpr, SQLAnyType elseExpr);
	/**
	 * Binary operation: SQL "IN".
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType in(SQLAnyType other);
	/**
	 * Binary operation: SQL "NOT IN".
	 * @param other Right side of the binary operation
	 * @return Result of the operation.
	 * @throws ConversionException When performing an illegal operation
	 */
	public abstract SQLAnyType notIn(SQLAnyType other);
	
	/**
	 * Get the type of this SQL value.
	 * @return {@link SQLAnyType.Type}
	 */
	public abstract Type getType();
		
	/**
	 * Cast to a Boolean.
	 * @return Result of casting
	 * @throws ConversionException When conversion is not allowed
	 */
	public abstract SQLBool convertToBoolType();
	/**
	 * Cast to an Integer.
	 * @return Result of casting
	 * @throws ConversionException When conversion is not allowed
	 */
	public abstract SQLInteger convertToIntegerType();
	
	/**
	 * Get the Z3 expression which determines whether this value should be NULL.
	 * @return {@link Z3.BoolExpr}
	 * @throws ConversionException When this Type cannot be NULL
	 */
	public abstract BoolExpr getNullExpr();
	/**
	 * Get all Z3 expressions available.
	 * @return Array with the Z3 expressions
	 */
	public abstract Expr[] getAllExpr();
	/**
	 * Substitutes all Z3 expressions and returns a new Object of the same type.
	 * @param from Z3 expressions to substitute from
	 * @param to Z3 expressions which replace the corresponding values from {@code from}
	 * @return Substituted SQLAnyType
	 */
	public abstract SQLAnyType substitute(Expr[] from, Expr[] to);
}
