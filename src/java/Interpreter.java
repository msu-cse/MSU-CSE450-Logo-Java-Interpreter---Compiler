import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class Interpreter {

	public static final Boolean FALSE = new Boolean(false);

	// -- Boolean truths
	public static final Integer ZERO = new Integer(0);
	Logger log = Logger.getLogger("Interpreter");

	/**
	 * Instantiate the interpreter based on an InputStream.
	 * 
	 * @param in
	 * @throws Exception
	 */
	public Interpreter(ANTLRStringStream in) throws Exception {
		// -- Parse the input
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

		// -- Execute the tree
		exec((TypeAwareTree) r.getTree());
	}

	public Interpreter(TypeAwareTree t) {
		exec(t);
	}

	Object add(TypeAwareTree t) {
		log.info("Adding" + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x + y;
		return z;
	}

	Object and(TypeAwareTree t) {
		log.info("and'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x && y;
	}

	Object block(TypeAwareTree t) {
		log.info("Executing block" + t.toStringTree());

		ScopeStack.getInstance().push(new Scope(t));
		
		for (TypeAwareTree child : t)
			exec(child);
		
		ScopeStack.getInstance().pop();

		return null;
	}

	Object div(TypeAwareTree t) {
		log.info("dividing " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x / y;
		return z;
	}

	private Object end(TypeAwareTree t) {
		// TODO Auto-generated method stub
		return null;
	}

	Object equality(TypeAwareTree t) {
		log.info("and'ing " + t.toStringTree());

		return exec(t.getChild(0)).equals(exec(t.getChild(1)));
	}

	Object exec(TypeAwareTree t) {
		
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
			return minus(t); // -
		case LogoTurtleParser.MODULO:
			return modulo(t); // %
		case LogoTurtleParser.MULT:
			return mult(t); // *
		case LogoTurtleParser.NOT:
			return negate(t); // !
		case LogoTurtleParser.NUMBER:
			return Integer.parseInt(t.getText());
		case LogoTurtleParser.OR:
			return or(t); // ||
		case LogoTurtleParser.PLUS:
			return add(t); // +
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

	Object greaterThan(TypeAwareTree t) {
		Object a = exec(t.getChild(0));
		Object b = exec(t.getChild(1));
		if (a instanceof Number && b instanceof Number) {

			Number x = (Number) a;
			Number y = (Number) b;

			return x.floatValue() > y.floatValue();

		}
		return false;
	}

	Object greaterThanEquals(TypeAwareTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x >= y;
	}

	String id(TypeAwareTree t) {
		return t.getText();
	}

	Object if_(TypeAwareTree t) {
		Object result = exec(t.getChild(0));

		ScopeStack.getInstance().push(new Scope(t));
		
		if (!(FALSE.equals(result) || ZERO.equals(result)))
			exec(t.getChild(1));

		ScopeStack.getInstance().pop();
		
		return null;
	}

	Object ifelse(TypeAwareTree t) {
		TypeAwareTree condition = t.getChild(0);
		TypeAwareTree iftrue = t.getChild(1);
		TypeAwareTree iffalse = t.getChild(2);

		Object result = exec(condition);

		ScopeStack.getInstance().push(new Scope(t));
		
		// Note that false what we test for
		if (FALSE.equals(result) || ZERO.equals(result)) {
			exec(iffalse);
		} else {
			exec(iftrue);
		}
		
		ScopeStack.getInstance().pop();
		
		return null;
	}

	Object lessThan(TypeAwareTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x < y;
	}

	Object lessThanEquals(TypeAwareTree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x <= y;
	}

	Object make(TypeAwareTree t) {
		String key = exec(t.getChild(0)).toString();
		Object value = exec(t.getChild(1));

		log.info("Setting " + key + " = " + value);

		ScopeStack.getInstance().put(key, value);

		return value;
	}

	Object minus(TypeAwareTree t) {
		log.info("subtracting " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x - y;
		return z;
	}

	Object modulo(TypeAwareTree t) {
		log.info("Modulo'ing " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x % y;

		return z;
	}

	Object mult(TypeAwareTree t) {
		log.info("Multiplying " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x * y;
		return z;
	}

	/**
	 * Returns the name of the specified symbol, e.g. <code>"asdf"</code> for 
	 * the ANTLR directive <code>"asdf</code>
	 * @param t
	 * @return
	 */
	Object name(TypeAwareTree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Encountered variable or string " + variableName);
		return variableName;
	}

	Object negate(TypeAwareTree t) {
		Boolean x = (Boolean) exec(t.getChild(0));
		return !x;
	}

	Object or(TypeAwareTree t) {
		log.info("or'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x || y;
	}

	Object print(TypeAwareTree t) {
		log.info("Printing " + t.toStringTree());

		// (print "x "y "z) will print multiple items, so there may be
		// many children. Only print a space if there are.
		String space = t.getChildCount() > 1 ? " " : "";

		for (TypeAwareTree subtree : t) {
			Object printValue = exec(subtree);
			log.info("Printing [" + printValue + "]");
			System.out.print(printValue + space);
		}

		System.out.println();

		return null;
	}

	private Object return_(TypeAwareTree t) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object to(TypeAwareTree t) {
		// TODO Auto-generated method stub
		return null;
	}

	void unhandledTypeError(TypeAwareTree t) {
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
	Object val(TypeAwareTree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Fetching value of " + variableName);
		return ScopeStack.getInstance().get(variableName).value;
	}

	Object while_(TypeAwareTree t) {
		TypeAwareTree condition = t.getChild(0);
		TypeAwareTree block = t.getChild(1);

		log.info("Executing while(" + condition.toStringTree() + "){"
				+ block.toStringTree() + "}");
		
		ScopeStack.getInstance().push(new Scope(t));
		
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
		
		ScopeStack.getInstance().pop();

		return null;
	}

	
}
