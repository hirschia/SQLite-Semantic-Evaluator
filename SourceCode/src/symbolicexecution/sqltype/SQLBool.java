package symbolicexecution.sqltype;

import com.microsoft.z3.BoolExpr;

/**
 * An abstract class for SQL Booleans.
 */
public abstract class SQLBool extends SQLAnyType {
	
	/** {@inheritDoc} */
	@Override
	public Type getType() {
		return Type.BOOL;
	}

	/**
	 * Get the Z3 expression for this boolean value.
	 * @return {@link Z3.BoolExpr}
	 */
	public abstract BoolExpr getBoolExpr();
}
