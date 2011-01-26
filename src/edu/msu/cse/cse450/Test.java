package edu.msu.cse.cse450;

import org.antlr.runtime.*;

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
        
        // begin parsing at rule program
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
