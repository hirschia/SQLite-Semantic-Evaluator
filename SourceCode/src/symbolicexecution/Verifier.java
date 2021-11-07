package symbolicexecution;

import java.util.function.Consumer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.sqlite.SQLiteLexer;
import parser.sqlite.SQLiteParser;

public class Verifier extends Thread {
	private Consumer<String> output;
	private String queries;
	
	public Verifier(String queries, Consumer<String> feedback) {
		this.queries = queries;
		this.output = feedback;
	}
	
	@Override
	public void run() {
		ParseTree tree = makeParseTree();
		Feedback feedback = new Feedback() {
			@Override
			public void processFeedback(int lineStart, int columnStart, int lineEnd, int columnEnd, String message,
					Type messageType) {
				String interval = String.format("At %d:%d-%d:%d:\n", lineStart, columnStart, lineEnd, columnEnd);
				String m = interval + message;
				output.accept(m);
			}
		};
		SQLite visitor = new SQLite(feedback);
		
		try {
			visitor.visit(tree);
		} catch (Exception e) {
			// TODO: handle exception properly
			output.accept(e.getMessage());
		}
		
		output.accept("Finished verification.");
	}
	
	private ParseTree makeParseTree() {
		SQLiteLexer lexer = new SQLiteLexer(CharStreams.fromString(queries));
		SQLiteParser parser = new SQLiteParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.parse();
		return tree;
	}
	
	public static void startVerification(String queries, Consumer<String> feedback) {
		new Verifier(queries, feedback).start();
	}

//	public static void main(String[] args) {
//		// TODO 
//	}
}
