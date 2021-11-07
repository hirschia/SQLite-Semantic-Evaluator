package symbolicexecution.sqlite.type;

import java.util.List;

import com.microsoft.z3.Context;

import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLRowValue;
import symbolicexecution.sqltype.SQLTypeFactory;

/**
 * Type factory for creating SQLite types.
 */
public class SQLiteTypeFactory extends SQLTypeFactory {
	private int tempCounter = 0;
	
	public SQLiteTypeFactory(Context z3Context) {
		super(z3Context);
	}

	/** {@inheritDoc} */
	@Override
	public SQLInteger makeIntegerLiteral(long integerLiteral) {
		return new SQLiteInteger(integerLiteral);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType makeNummericLiteral(String nummericLiteral) {
		// Currently only supporting integers.
		long value = Long.parseLong(nummericLiteral);
		return new SQLiteInteger(value);
	}

	/** {@inheritDoc} */
	@Override
	public SQLInteger makeInteger(String name) {
		String tempRef = "_int_null_" + String.valueOf(tempCounter++);
		return new SQLiteInteger(name, tempRef);
	}

	/** {@inheritDoc} */
	@Override
	public SQLAnyType makeNullLiteral() {
		Context ctx = SQLAnyType.getZ3Context();
		return new SQLiteInteger(ctx.mkInt(0), ctx.mkBool(true));
	}

	/** {@inheritDoc} */
	@Override
	public SQLRowValue makeRowValue(List<SQLAnyType> elements) {
		return new SQLiteRowValue(elements);
	}

}
