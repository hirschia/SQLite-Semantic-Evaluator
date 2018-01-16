/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.StringReader;
import java.lang.*;
import javax.swing.JOptionPane;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import sqlitesyntax.grammar.SQLiteSyntaxLexer;
import sqlitesyntax.grammar.SQLiteSyntaxParser;
import sqlitesyntax.grammar.SQLiteSyntaxParser.ParseContext;


/**
 *
 * @author Piyush
 */
public class CheckSyntax {
    private static String st="";

    public static String testing(String s) throws IOException {
        CharStream inputCharStream = new ANTLRInputStream(new StringReader(s));
         SQLiteSyntaxLexer lexer = new SQLiteSyntaxLexer(inputCharStream);
        CommonTokenStream inputTokenStream = new CommonTokenStream(lexer);
        SQLiteSyntaxParser parser = new SQLiteSyntaxParser(inputTokenStream);
        
        st="No Errors";
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
        	@Override
        	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        	//Do whatever you want to do on recognizing error.
                    st=msg;
		}
	});
        
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
        	@Override
        	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        	//Do whatever you want to do on recognizing error.
                    st=msg;
		}
	});

        //JOptionPane.showMessageDialog(null, s);
        ParseContext context = parser.parse();
        return st;
    }
    
}
