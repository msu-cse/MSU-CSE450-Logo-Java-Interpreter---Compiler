package edu.msu.cse.cse450;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;



public class Interpreter {
	Logger log = Logger.getLogger("Interpreter");
	HashMap<String,Object> memory = new HashMap<String,Object>();
	
	/**
	 * Instantiate the interpreter based on an InputStream.
	 * @param in
	 * @throws Exception 
	 */
	public Interpreter(ANTLRStringStream in) throws Exception {
		// -- Parse the inpiut
		LogoAST2Lexer lexer = new LogoAST2Lexer(in);
		TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		LogoAST2Parser parser = new LogoAST2Parser(tokens);
		
		// -- 'program' is the top-level rule
		LogoAST2Parser.program_return r = parser.program();
		
		// -- Check for errors
		if(parser.getNumberOfSyntaxErrors() != 0)
			throw new Exception("There were " + parser.getNumberOfSyntaxErrors() + " syntax errors.");
		
		// -- Execute the tree
		exec((CommonTree)r.getTree());
	}
	
	public Interpreter(CommonTree t) {
		exec(t);
	}
	
	void exec(CommonTree t) {
		
		log.info( "Parsing tree " + t.toStringTree() );
		return;
		/*
		try {
			switch (t.getType()) {
			case LogoAST2Parser.BLOCK:
			case LogoAST2Parser.MAKE:
				break;
			}
		} finally {}
		*/
	}
	
	
}
