package symbolicexecution.sqltype;

import com.microsoft.z3.IntExpr;

/**
 * Abstract class for SQL Integers.
 */
public abstract class SQLInteger extends SQLAnyType {
	
	/** {@inheritDoc} */
	@Override
	public Type getType() {
		return Type.INTEGER;
	}
	
	/**
	 * Get the Z3 expression for this integer.
	 * @return {@link Z3.IntExpr}
	 */
	public abstract IntExpr getIntExpr();
}
