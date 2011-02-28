import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class Interpreter {
	Logger log = Logger.getLogger("Interpreter");

	HashMap<String, Object> memory = new HashMap<String, Object>();

	// -- Boolean truths
	public static final Integer ZERO = new Integer(0);
	public static final Boolean FALSE = new Boolean(false);

	/**
	 * Implements the {@link Iterable} pattern for {@link Tree}.
	 * 
	 * @note This class would normally be in a completely separate file, but we
	 *       can only turn in specific files for the project.
	 * 
	 * @author riggleza
	 */
	class IterableTree implements Iterable<Tree> {

		/**
		 * Implements the {@link Iterator} pattern for {@link Tree}.
		 * 
		 * @author riggleza
		 */
		class TreeIterator implements Iterator<Tree> {
			Tree t;
			Integer current = -1;

			public TreeIterator(Tree t) {
				this.t = t;
			}

			@Override
			public boolean hasNext() {
				return (current + 1) < t.getChildCount();
			}

			@Override
			public Tree next() {
				return t.getChild(++current);
			}

			@Override
			public void remove() {
				t.deleteChild(current--);
			}
		}

		Tree t;

		public IterableTree(Tree t) {
			this.t = t;
		}

		@Override
		public Iterator<Tree> iterator() {
			return new TreeIterator(t);
		}

	}

	/**
	 * Instantiate the interpreter based on an InputStream.
	 * 
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
		if (parser.getNumberOfSyntaxErrors() != 0)
			throw new Exception("There were "
					+ parser.getNumberOfSyntaxErrors() + " syntax errors.");

		// -- Execute the tree
		exec((CommonTree) r.getTree());
	}

	public Interpreter(CommonTree t) {
		exec(t);
	}

	Object exec(Tree t) {
		switch (t.getType()) {
		case 0: // Nil, root of the tree, fall through to 'block'
		case LogoAST2Parser.BLOCK:
			return block(t);

		case LogoAST2Parser.AND:
			return and(t); // &&
		case LogoAST2Parser.BYNAME:
			return name(t);
		case LogoAST2Parser.BYVAL:
			return val(t);
		case LogoAST2Parser.DIV:
			return div(t); // /
		case LogoAST2Parser.EQ:
			return equality(t); // ==
		case LogoAST2Parser.GT:
			return greaterThan(t); // >
		case LogoAST2Parser.GTE:
			return greaterThanEquals(t); // >=
		case LogoAST2Parser.ID:
			return id(t);
		case LogoAST2Parser.IF:
			unhandledTypeError(t);
		case LogoAST2Parser.IFELSE:
			return ifelse(t);
		case LogoAST2Parser.LT:
			return lessThan(t); // <
		case LogoAST2Parser.LTE:
			return lessThanEquals(t); // <=
		case LogoAST2Parser.MAKE:
			return make(t);
		case LogoAST2Parser.MINUS:
			return minus(t); // -
		case LogoAST2Parser.MODULO:
			return modulo(t); // %
		case LogoAST2Parser.MULT:
			return mult(t); // *
		case LogoAST2Parser.NOT:
			return negate(t); // !
		case LogoAST2Parser.NUMBER:
			return Integer.parseInt(t.getText());
		case LogoAST2Parser.OR:
			return or(t); // ||
		case LogoAST2Parser.PLUS:
			return add(t); // +
		case LogoAST2Parser.PRINT:
			return print(t);
		case LogoAST2Parser.WHILE:
			return while_(t);
		default:
			unhandledTypeError(t);
		}

		return null;
	}

	Object lessThan(Tree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x < y;
	}

	Object lessThanEquals(Tree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x <= y;
	}

	Object minus(Tree t) {
		log.info("subtracting " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x - y;
		return z;
	}

	Object or(Tree t) {
		log.info("or'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x || y;
	}

	Object greaterThan(Tree t) {
		Object a = exec((CommonTree) t.getChild(0));
		Object b = exec((CommonTree) t.getChild(1));
		if (a instanceof Number && b instanceof Number) {

			Number x = (Number) a;
			Number y = (Number) b;

			return x.floatValue() > y.floatValue();

		}
		return false;
	}

	Object greaterThanEquals(Tree t) {
		log.info("evaluating " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		return x >= y;
	}

	Object div(Tree t) {
		log.info("dividing " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x / y;
		return z;
	}

	Object and(Tree t) {
		log.info("and'ing " + t.toStringTree());
		Boolean x = (Boolean) exec(t.getChild(0));
		Boolean y = (Boolean) exec(t.getChild(1));
		return x && y;
	}

	void unhandledTypeError(Tree t) {
		log.severe("Encountered unhandled type " + t.getType() + " ("
				+ LogoAST2Parser.tokenNames[t.getType()] + ")" + " at \""
				+ t.getText() + "\"" + " on line " + t.getLine() + ":"
				+ t.getCharPositionInLine());
		System.exit(1);
	}

	String id(Tree t) {
		return t.getText();
	}

	Object name(Tree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Encountered variable or string " + variableName);
		return variableName;
	}

	Object val(Tree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Fetching value of " + variableName);
		return memory.get(variableName);
	}

	Object print(Tree t) {
		log.info("Printing " + t.toStringTree());

		// (print "x "y "z) will print multiple items, so there may be
		// many children. Only print a space if there are.
		String space = t.getChildCount() > 1 ? " " : "";

		for (Tree subtree : new IterableTree(t)) {
			Object printValue = exec(subtree);
			log.info("Printing [" + printValue + "]");
			System.out.print(printValue + space);
		}

		System.out.println();

		return null;
	}

	Object if_(Tree t) {
		Object result = exec(t.getChild(0));

		if (FALSE.equals(result) || ZERO.equals(result))
			return Boolean.FALSE;

		else
			exec(t.getChild(1));

		return Boolean.TRUE;
	}

	Object ifelse(Tree t) {
		Tree condition = t.getChild(0);
		Tree iftrue = t.getChild(1);
		Tree iffalse = t.getChild(2);

		Object result = exec(condition);

		// Note that false what we test for
		if (FALSE.equals(result) || ZERO.equals(result)) {
			exec(iffalse);
			return Boolean.FALSE;
		} else {
			exec(iftrue);
			return Boolean.TRUE;
		}
	}

	Object while_(Tree t) {
		Tree condition = t.getChild(0);
		Tree block = t.getChild(1);

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

	Object block(Tree t) {
		log.info("Executing block" + t.toStringTree());

		for (Tree child : new IterableTree(t))
			exec(child);

		return null;
	}

	Object make(Tree t) {
		String key = exec(t.getChild(0)).toString();
		Object value = exec(t.getChild(1));

		log.info("Setting " + key + " = " + value);

		memory.put(key, value);

		return value;
	}

	Object negate(Tree t) {
		Boolean x = (Boolean) exec(t.getChild(0));
		return !x;
	}

	Object mult(Tree t) {
		log.info("Multiplying " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x * y;
		return z;
	}

	Object modulo(Tree t) {
		log.info("Modulo'ing " + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x % y;

		return z;
	}

	Object add(Tree t) {
		log.info("Adding" + t.toStringTree());
		int x = (Integer) exec(t.getChild(0));
		int y = (Integer) exec(t.getChild(1));
		int z = x + y;
		return z;
	}

	Object equality(Tree t) {
		log.info("and'ing " + t.toStringTree());

		return exec(t.getChild(0)).equals(exec(t.getChild(1)));
	}

}
