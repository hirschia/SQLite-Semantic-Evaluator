package symbolicexecution;

public class Fact {
	private final String identfier;
	private final int statusCode;
	private final Type type;
	
	public enum Type {
		UNKNOWN,
		LITERAL_BOOL,
		LITERAL_INT,
		LITERAL_NULL,
		BOOL,
		INT,
		ROWVALUE
	}
	
	/** 
	 * Status code: Nothing special
	 */
	public static final int OK = 0; 
	/** 
	 * Status code: Expression can only be NULL
	 */
	public static final int JUST_NULL = 1;
	/** 
	 * Status code: Expression can contain NULL
	 */
	public static final int MAY_CONTAIN_NULL = 2;
	/** 
	 * Status code: Boolean expression is tautology
	 */
	public static final int TAUTOLOGY = 4;
	/** 
	 * Status code: Boolean expression is unsatisfiable
	 */
	public static final int UNSATISFIABLE = 8;
	/** 
	 * Status code: Integer expression is zero
	 */
	public static final int ZERO = 16;
	/** 
	 * Status code: Integer expression is one
	 */
	public static final int ONE = 32;
	/** 
	 * Status code: Integer expression cannot be zero
	 */
	public static final int NONZERO = 64;
	/**
	 * Status code: Constant expression
	 */
	public static final int CONSTANT = 128;
	/**
	 * Status code: Inequality is strictly unequal
	 */
	public static final int EQUALITY_UNNECESSARY = 256;
	/**
	 * Status code: Inequality is equivalent to equality
	 */
	public static final int JUST_EQUALITY = 512;
	/**
	 * Status code: Left expression is unnecessary
	 */
	public static final int LEFT_UNNECESSARY = 1024;
	/**
	 * Status code: Right expression is unnecessary
	 */
	public static final int RIGHT_UNNECESSARY = 2048;
	
	public Fact(String identifier, int statusCode, Type type) {
		this.identfier = identifier;
		this.statusCode = statusCode;
		this.type = type;
	}
	public Fact(String identifier, int statusCode) {
		this(identifier, statusCode, Type.UNKNOWN);
	}
	
	public String getIdentifier() { return identfier; }
	public int getStatusCode() { return statusCode; }
	public Type getType() { return type; }
	
	public boolean isTautology() { 
		return compareNotZero(TAUTOLOGY); 
	}
	public boolean isUnsatisfiable() {
		return compareNotZero(UNSATISFIABLE);
	}
	public boolean isTautologyExtended() {
		int cmpValue = TAUTOLOGY | NONZERO;
		int cmpNull = JUST_NULL | MAY_CONTAIN_NULL;
		return compareZero(cmpNull) && compareNotZero(cmpValue);
	}
	public boolean isUnsatisfiableExtended() {
		return compareNotZero(ZERO | JUST_NULL | UNSATISFIABLE);
	}
	public boolean canContainNull() {
		return compareNotZero(MAY_CONTAIN_NULL | JUST_NULL);
	}
	public boolean isConst() {
		return compareNotZero(CONSTANT);
	}
	public boolean isConstOrLiteral() {
		return isConst() || isLiteral();
	}
	public boolean isConstZero() {
		return compareNotZero(ZERO | UNSATISFIABLE) && compareZero(JUST_NULL | MAY_CONTAIN_NULL);
	}
	public boolean isLiteral() {
		boolean result = (type == Type.LITERAL_BOOL) ||
				(type == Type.LITERAL_INT) ||
				(type == Type.LITERAL_NULL);
		return result;
	}
	public boolean isJustNull() {
		return compareNotZero(JUST_NULL);
	}
	public boolean isTypeLiteral() {
		switch (this.type) {
		case LITERAL_BOOL:
		case LITERAL_INT:
		case LITERAL_NULL:
			return true;
		default:
			return false;
		}
	}
	public boolean isStrictInequality() {
		return compareNotZero(EQUALITY_UNNECESSARY);
	}
	public boolean isStrictEquality() {
		return compareNotZero(JUST_EQUALITY);
	}
	public boolean isLeftExprUnnecessary() {
		return compareNotZero(LEFT_UNNECESSARY);
	}
	public boolean isRightExprUnnecessary() {
		return compareNotZero(RIGHT_UNNECESSARY);
	}
	
	protected boolean compareNotZero(int value) {
		int result = this.statusCode & value;
		return result != 0;
	}
	protected boolean compareZero(int value) {
		int result = this.statusCode & value;
		return result == 0;
	}
	
}
