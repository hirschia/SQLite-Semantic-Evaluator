package symbolicexecution.sqltype;

public abstract class SQLRowValue extends SQLAnyType {
	
	public Type getType() {
		return Type.ROW_VALUE;
	}
	
	public abstract SQLRowValue setUnion(SQLAnyType other);
	public abstract SQLAnyType[] getRowContent();
	public abstract int getRowSize();
}
