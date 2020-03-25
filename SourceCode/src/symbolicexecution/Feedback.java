package symbolicexecution;

public interface Feedback {
	public static final int NOENTRY = -1;
	
	public enum Type {
		MESSAGE,
		LOG,
		// TODO
	}
	
	public abstract void processFeedback(int lineStart, int columnStart, int lineEnd, int columnEnd, String message, Type messageType);
	
	public default void processFeedback(String message, Type messageType) {
		processFeedback(NOENTRY, NOENTRY, NOENTRY, NOENTRY, message, messageType);
	}
	
	public default void processFeedback(String message) {
		processFeedback(message, Type.MESSAGE);
	}
}
