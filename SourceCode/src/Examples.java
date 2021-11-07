import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.sqlite.SQLiteLexer;
import parser.sqlite.SQLiteParser;
import symbolicexecution.Feedback;
import symbolicexecution.SQLite;
import symbolicexecution.Feedback.Type;

public class Examples {
	
	public static String basetable = 
			"CREATE TABLE T (\n" + 
			"x,\n" + 
			"y,\n" + 
			"z,\n" +
			"CHECK(y < 42 AND (z NOTNULL))\n" + 
			")\n\n";
	
	public static String query1 =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE x < 0 AND\n" + 
			"x > 5";
	
	public static String query8b =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE z < 5 OR\n" + 
			"z > 0";
	
	public static String query8c =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE x > 5 OR\n" + 
			"x > 0";
	
	public static String query8d =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE x = 0 OR\n" + 
			"y < 200";
	
	public static String query8e =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE x < 0 AND\n" + 
			"y = 42";
	
	public static String query8f =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE z < 0 AND\n" + 
			"y < 200";
	
	public static String query9 =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE x != NULL";
	
	public static String query11 =
			"SELECT *\n" + 
			"FROM T\n" + 
			"WHERE z >= 0 AND\n" + 
			"z <= 0";
	
	public static String query43 =
			"UPDATE T \n" + 
			"SET y = y + 2";
	
	public static String query_integer_truncated =
			"CREATE TABLE employee (salary int);\n\n" +
			"UPDATE employee\n" +
			"SET salary = salary * (5/4);";
	
	public static String[][] queries = {
			{"q1", basetable + query1}, 
			{"q8b", basetable + query8b}, 
			{"q8c", basetable + query8c}, 
			{"q8d", basetable + query8d}, 
			{"q8e", basetable + query8e}, 
			{"q8f", basetable + query8f}, 
			{"q9", basetable + query9},
			{"q11", basetable + query11},
			{"q43", basetable + query43},
			{"integer_truncated", query_integer_truncated}
	};
	
	public static void runQuery(String query, boolean showTiming) {
		long start, afterParse, end;
		
		start = System.currentTimeMillis();
		SQLiteLexer lexer = new SQLiteLexer(CharStreams.fromString(query));
		SQLiteParser parser = new SQLiteParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.parse();
		SQLite visitor = new SQLite(
			new Feedback() {
				@Override
				public void processFeedback(int lineStart, int columnStart, int lineEnd, int columnEnd, String message,
						Type messageType) {
					System.out.println(String.format("At %d:%d-%d:%d: %s", lineStart, columnStart, lineEnd, columnEnd, message));
				}
			});
		afterParse = System.currentTimeMillis();
		visitor.visit(tree);
		end = System.currentTimeMillis();
		
		if (showTiming)
			System.out.println(String.format("Timing:\nParsing: %dms\nEvaluation: %dms\nTotal: %dms\n", afterParse-start, end-afterParse, end-start));
	}
	
	public static void runQueries(boolean showTiming, boolean printQuery) {
		for (String[] query : queries) {
			System.out.println("*************************");
			System.out.println(String.format("Query %s:", query[0]));
			System.out.println("*************************");
			if (printQuery) {
				System.out.println(String.format("Query:\n%s\n\nResults:", query[1]));
			}
			runQuery(query[1], showTiming);
		}
	}

	/**
	 * Runs a few example queries.
	 * See Examples_output.txt for the output of a run.
	 */
	public static void main(String[] args) {
		runQueries(true, true);
	}

}
