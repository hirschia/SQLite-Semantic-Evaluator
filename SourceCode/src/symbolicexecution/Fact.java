package symbolicexecution;

public class Fact {
	private final String identfier;
	private final int statusCode;
	
	public Fact(String identifier, int statusCode) {
		this.identfier = identifier;
		this.statusCode = statusCode;
	}
	
	public String getIdentifier() { return identfier; }
	public int getStatusCode() { return statusCode; }
	
}
