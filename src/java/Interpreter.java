import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Interpreter {

	public static final Boolean FALSE = new Boolean(false);

	// -- Boolean truths
	public static final Integer ZERO = new Integer(0);

	private static Interpreter instance = null;
	Logger log = Logger.getLogger("Interpreter");
	
	static Interpreter getInstance() {
		return instance ;
	}

	/**
	 * Instantiate the interpreter based on an InputStream.
	 * 
	 * @param in
	 * @throws Exception
	 */
	public Interpreter(ANTLRStringStream in) throws Exception {
		instance = this;
		
		// -- Parse the input`
		LogoTurtleLexer lexer = new LogoTurtleLexer(in);
		TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		LogoTurtleParser parser = new LogoTurtleParser(tokens);
		parser.setTreeAdaptor(new LogoASTAdaptor());

		// -- 'program' is the top-level rule
		LogoTurtleParser.program_return r = parser.program();

		// -- Check for errors
		if (parser.getNumberOfSyntaxErrors() != 0)
			throw new Exception("There were "
					+ parser.getNumberOfSyntaxErrors() + " syntax errors.");

		// -- Create the global scope
		ScopedTree t = (ScopedTree) r.getTree();
		t.memorySpace = new HashMap<String,Symbol>();
		
		// -- Execute
		log.info("Full AST: " + t.toStringTree());
		exec(t);
	}

	public Interpreter(ScopedTree t) {
		instance = this;
		
		exec(t);
	}

	Object block(ScopedTree t) {
		log.info("Executing block" + t.toStringTree());
		
		for (ScopedTree child : t)
			exec(child);
		
		return null;
	}

	private Object end(ScopedTree t) {
		// TODO Auto-generated method stub
		return null;
	}

	Object equality(ScopedTree t) {
		log.info("=='ing " + t.toStringTree());

		// I think that we can let Java do its .equals magic.
		Object left =  exec(t.getChild(0));
		Object right = exec(t.getChild(1));
		
		return left.equals(right);
	}

	static Object e(ScopedTree t) {
		return getInstance().exec(t);
	}
	
	Object exec(ScopedTree t) {
		
		log.info("Executing " + t.toStringTree());
		
		switch (t.getType()) {
		case 0: // Nil, root of the tree, fall through to 'block'
		case LogoTurtleParser.BLOCK:
			return block(t);

		case LogoTurtleParser.AND:
			return LogoLogic.op(t); // &&
		case LogoTurtleParser.BYNAME:
			return name(t);
		case LogoTurtleParser.BYVAL:
			return val(t);
		case LogoTurtleParser.DIV:
			return LogoMath.op(t); // /
		case LogoTurtleParser.EQ:
			return equality(t); // ==
		case LogoTurtleParser.END:
			return end(t);
		case LogoTurtleParser.FLOAT:
			return Float.parseFloat(t.getText());
		case LogoTurtleParser.GT:
			return LogoMath.op(t); // >
		case LogoTurtleParser.GTE:
			return LogoMath.op(t); // >=
		case LogoTurtleParser.ID:
			return id(t);
		case LogoTurtleParser.IF:
			return if_(t);
		case LogoTurtleParser.IFELSE:
			return ifelse(t);
		case LogoTurtleParser.LT:
			return LogoMath.op(t); // <
		case LogoTurtleParser.LTE:
			return LogoMath.op(t); // <=
		case LogoTurtleParser.MAKE:
			return make(t);
		case LogoTurtleParser.MINUS:
			return LogoMath.op(t); // -
		case LogoTurtleParser.MODULO:
			return LogoMath.op(t); // %
		case LogoTurtleParser.MULT:
			return LogoMath.op(t); // *
		case LogoTurtleParser.NOT:
			return LogoLogic.op(t); // !
		case LogoTurtleParser.INTEGER:
			return Integer.parseInt(t.getText());
		case LogoTurtleParser.OR:
			return LogoLogic.op(t); // ||
		case LogoTurtleParser.PLUS:
			return LogoMath.op(t); // +
		case LogoTurtleParser.PRINT:
			return print(t);
		case LogoTurtleParser.RETURN:
			return return_(t);
		case LogoTurtleParser.TO:
			return to(t);
		case LogoTurtleParser.WHILE:
			return while_(t);
		default:
			unhandledTypeError(t);
		}

		return null;
	}

	Object id(ScopedTree t) {
		if(t instanceof CallNode) {			
			CallNode cn = (CallNode) t;
			Symbol s = cn.get(t.getText(), cn);
			log.info(s.getClass().getName());
			if(s.getValue() instanceof Function) {
				Function f = (Function) s.getValue();
				
				cn.calledFunction = f;
					
				try {
					f.call(cn, cn.getChildren());
				}
				catch (ReturnException re) {
					return cn.calledFunction.returnValue;
				}	
			} else {
				throw new RuntimeException("Tried to call " + t.getText() + " which is not a function.");
			}
		}
		
		// -- Just a simple ID
		return t.getText();
	}

	Object if_(ScopedTree t) {
		Object result = exec(t.getChild(0));
		
		if (!(FALSE.equals(result) || ZERO.equals(result)))
			exec(t.getChild(1));
		
		return null;
	}

	Object ifelse(ScopedTree t) {
		
		ScopedTree condition = t.getChild(0);
		ScopedTree iftrue = t.getChild(1);
		ScopedTree iffalse = t.getChild(2);

		Object result = exec(condition);
		
		// Note that false what we test for
		if (FALSE.equals(result) || ZERO.equals(result)) {
			exec(iffalse);
		} else {
			exec(iftrue);
		}
		
		return null;
	}

	Object make(ScopedTree t) {
		String key = exec(t.getChild(0)).toString();
		Object value = exec(t.getChild(1));

		log.info("Setting " + key + " = " + value);
		
		t.put(key, value);

		return value;
	}

	/**
	 * Returns the name of the specified symbol, e.g. <code>"asdf"</code> for 
	 * the ANTLR directive <code>"asdf</code>
	 * @param t
	 * @return
	 */
	Object name(ScopedTree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Encountered variable or string " + variableName);
		return variableName;
	}

	Object print(ScopedTree t) {
		log.info("Printing " + t.toStringTree());

		// (print "x "y "z) will print multiple items, so there may be
		// many children. Only print a space if there are.
		String space = t.getChildCount() > 1 ? " " : "";

		for (ScopedTree subtree : t) {
			Object printValue = exec(subtree);
			log.info("Printing [" + printValue + "]");
			System.out.print(printValue + space);
		}

		System.out.println();

		return null;
	}

	private Object return_(ScopedTree t) {
		
		return null;
	}

	private Object to(ScopedTree t) {
		Function f = new Function(t);
		t.put(f.getName(), f);
		
		log.info(f.getClass().getName());
		
		Object otherF = t.get(f.getName(), t).getClass().getName();
		log.info(otherF.toString());
		
		log.info(new Boolean(f==otherF).toString());
		
		return f;
	}

	void unhandledTypeError(ScopedTree t) {
		log.severe("Encountered unhandled type " + t.getType() + " ("
				+ LogoTurtleParser.tokenNames[t.getType()] + ")" + " at \""
				+ t.getText() + "\"" + " on line " + t.getLine() + ":"
				+ t.getCharPositionInLine());
		System.exit(1);
	}

	/**
	 * Returns the value of the symbol.
	 * @param t
	 * @return
	 */
	Object val(ScopedTree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Fetching value of " + variableName);
		Object value = t.get(variableName,t).getValue();
		log.info("... " + value);
		return value;
	}

	Object while_(ScopedTree t) {
		
		ScopedTree condition = t.getChild(0);
		ScopedTree block = t.getChild(1);

		log.info("Executing while(" + condition.toStringTree() + "){"
				+ block.toStringTree() + "}");
				
		while (true) {
			log.info("Testing condition " + condition.toStringTree());
			Object result = exec(condition);

			if (ZERO.equals(result)) {
				log.info(condition.toStringTree()
						+ " evaluates to ZERO.  Exiting loop "
						+ t.toStringTree());
				break;
			} else if (FALSE.equals(result)) {
				log.info(condition.toStringTree()
						+ " evaluates to FALSE.  Exiting loop "
						+ t.toStringTree());
				break;
			} else {
				exec(block);
			}
		}
		
		return null;
	}

	
}
