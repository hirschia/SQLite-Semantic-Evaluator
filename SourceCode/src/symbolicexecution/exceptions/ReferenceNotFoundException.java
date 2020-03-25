package symbolicexecution.exceptions;

@SuppressWarnings("serial")
public class ReferenceNotFoundException extends RuntimeException {
	private final String ref;
	
	public ReferenceNotFoundException(String reference) {
		super("Reference String \"" + reference + "\" was not found.\n");
		this.ref = reference;
	}
	
	public String getReference() {
		return ref;
	}
}
