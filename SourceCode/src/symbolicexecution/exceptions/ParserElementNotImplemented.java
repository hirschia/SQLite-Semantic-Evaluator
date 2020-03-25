package symbolicexecution.exceptions;

public class ParserElementNotImplemented extends RuntimeException {
	public ParserElementNotImplemented(String content) {
		super("The ParseTreeVisitor has currently no implemented support for:\n" + content + "\n");
	}
}
