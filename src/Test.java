/***
 * Excerpted from "The Definitive ANTLR Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr for more book information.
***/
import org.antlr.runtime.*;
import org.python.antlr.LogoTokensLexer;

import edu.msu.cse.cse450.LogoTokensParser;

public class Test {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        // create a lexer that feeds off of input CharStream
        LogoTokensLexer lexer = new LogoTokensLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        LogoTokensParser parser = new LogoTokensParser(tokens);
        
        // begin parsing at rule r
        parser.program();
        
        System.out.println("COMMANDS:       " + lexer.commandCount);
        System.out.println("IDS:            " + lexer.idCount);
        System.out.println("NUMBERS:        " + lexer.numberCount);
        System.out.println("MATHOPS:        " + lexer.mathopCount);
        System.out.println("REFOPS:         " + lexer.refopCount);
        System.out.println("NEWLINES:       " + lexer.newlineCount);
        System.out.println("COMMENTS:       " + lexer.commentCount);
        
    }
}
