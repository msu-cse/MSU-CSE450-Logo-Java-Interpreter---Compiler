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
		
		exec(t);
	}

	public Interpreter(ScopedTree t) {
		instance = this;
		
		exec(t);
	}

	Object and(ScopedTree t) {
		log.info("and'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x && y;
	}

	Object block(ScopedTree t) {
		log.info("Executing block" + t.toStringTree());
		
		for (ScopedTree child : t)
			exec(child);
		
		return null;
	}

	Object div(ScopedTree t) {
		log.info("dividing " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x / y;
		return z;
	}

	private Object end(ScopedTree t) {
		// TODO Auto-generated method stub
		return null;
	}

	Object equality(ScopedTree t) {
		log.info("and'ing " + t.toStringTree());

		return exec(t.getChild(0)).equals(exec(t.getChild(1)));
	}

	static Object e(ScopedTree t) {
		return getInstance().exec(t);
	}
	
	Object exec(ScopedTree t) {
		
		log.info("Executing " + t.toString());
		
		switch (t.getType()) {
		case 0: // Nil, root of the tree, fall through to 'block'
		case LogoTurtleParser.BLOCK:
			return block(t);

		case LogoTurtleParser.AND:
			return and(t); // &&
		case LogoTurtleParser.BYNAME:
			return name(t);
		case LogoTurtleParser.BYVAL:
			return val(t);
		case LogoTurtleParser.DIV:
			return div(t); // /
		case LogoTurtleParser.EQ:
			return equality(t); // ==
		case LogoTurtleParser.END:
			return end(t);
		case LogoTurtleParser.FLOAT:
			return Float.parseFloat(t.getText());
		case LogoTurtleParser.GT:
			return greaterThan(t); // >
		case LogoTurtleParser.GTE:
			return greaterThanEquals(t); // >=
		case LogoTurtleParser.ID:
			return id(t);
		case LogoTurtleParser.IF:
			return if_(t);
		case LogoTurtleParser.IFELSE:
			return ifelse(t);
		case LogoTurtleParser.LT:
			return lessThan(t); // <
		case LogoTurtleParser.LTE:
			return lessThanEquals(t); // <=
		case LogoTurtleParser.MAKE:
			return make(t);
		case LogoTurtleParser.MINUS:
			return LogoMath.op(t); // -
		case LogoTurtleParser.MODULO:
			return LogoMath.op(t); // %
		case LogoTurtleParser.MULT:
			return LogoMath.op(t); // *
		case LogoTurtleParser.NOT:
			return negate(t); // !
		case LogoTurtleParser.INTEGER:
			return Integer.parseInt(t.getText());
		case LogoTurtleParser.OR:
			return or(t); // ||
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

	Object greaterThan(ScopedTree t) {
		Object a = exec(t.getChild(0));
		Object b = exec(t.getChild(1));
		if (a instanceof Number && b instanceof Number) {

			Number x = (Number) a;
			Number y = (Number) b;

			return x.floatValue() > y.floatValue();

		}
		return false;
	}

	Object greaterThanEquals(ScopedTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x >= y;
	}

	String id(ScopedTree t) {
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

	Object lessThan(ScopedTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x < y;
	}

	Object lessThanEquals(ScopedTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x <= y;
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

	Object negate(ScopedTree t) {
		Boolean x = (Boolean) exec(t.getChild(0));
		return !x;
	}

	Object or(ScopedTree t) {
		log.info("or'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x || y;
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
		// TODO Auto-generated method stub
		return null;
	}

	private Object to(ScopedTree t) {
		// TODO Auto-generated method stub
		return null;
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
		return t.get(variableName,t);
	}

	Object while_(ScopedTree t) {
		ScopedTree condition = t.getChild(0);
		ScopedTree block = t.getChild(1);

		log.info("Executing while(" + condition.toStringTree() + "){"
				+ block.toStringTree() + "}");
		
		while (true) {
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
