package edu.msu.cse.cse450;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;

public class Test {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
    	ANTLRStringStream input;
    	if(args.length > 0)
    		input = new ANTLRStringStream(args[0]);
    	else
    		input = new ANTLRInputStream(System.in);

        // create a lexer that feeds off of input CharStream
        LogoTokensLexer lexer = new LogoTokensLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        LogoTokensParser parser = new LogoTokensParser(tokens);
        
        // begin parsing at rule program, get tree
        RuleReturnScope scope = parser.program();
        CommonTree tree = (CommonTree) scope.getTree();
        
        System.out.print("AST: ");
        System.out.println(tree.toStringTree()); // print the tree
    }
}
