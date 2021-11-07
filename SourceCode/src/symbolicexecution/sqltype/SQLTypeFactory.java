package symbolicexecution.sqltype;

import java.util.List;

import com.microsoft.z3.Context;

/**
 * Abstract class used as an SQL type factory.
 */
public abstract class SQLTypeFactory {
	
	public SQLTypeFactory(Context Z3Context) {
		SQLAnyType.setZ3Context(Z3Context);
	}
	
	/**
	 * Creates a new integer literal.
	 * @param integerLiteral Value of the integer
	 * @return
	 */
	public abstract SQLInteger makeIntegerLiteral(long integerLiteral);
	/**
	 * Creates a new number literal.
	 * @param nummericLiteral Value of the number
	 * @return
	 */
	public abstract SQLAnyType makeNummericLiteral(String nummericLiteral);
	/**
	 * Creates a new integer with variable content (e.g. a table column).
	 * @param name Name of the integer
	 * @return
	 */
	public abstract SQLInteger makeInteger(String name);
	/**
	 * Creates a new NULL literal value.
	 * @return
	 */
	public abstract SQLAnyType makeNullLiteral();
	/**
	 * Creates a new row value.
	 * @param elements List of values stored in the row value
	 * @return
	 */
	public abstract SQLRowValue makeRowValue(List<SQLAnyType> elements);
	
}
