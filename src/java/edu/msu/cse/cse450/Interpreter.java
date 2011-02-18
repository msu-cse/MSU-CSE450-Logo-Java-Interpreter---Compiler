package edu.msu.cse.cse450;

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
			private Tree t;
			private Integer current = -1;

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

		private Tree t;

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
		log.info("Parsing tree " + t.toStringTree());

		switch (t.getType()) {
		case 0: // Nil, root of the tree, fall through to 'block'
		case LogoAST2Parser.BLOCK:
			return block(t);

		case LogoAST2Parser.AND:
			unhandledTypeError(t); // &&
		case LogoAST2Parser.BYNAME:
			return name(t);
		case LogoAST2Parser.BYVAL:
			return val(t);
		case LogoAST2Parser.DIV:
			unhandledTypeError(t); // /
		case LogoAST2Parser.EQ:
			unhandledTypeError(t); // ==
		case LogoAST2Parser.GT:
			unhandledTypeError(t); // >
		case LogoAST2Parser.GTE:
			unhandledTypeError(t); // >=
		case LogoAST2Parser.ID:
			return id(t);
		case LogoAST2Parser.IF:
			unhandledTypeError(t);
		case LogoAST2Parser.IFELSE:
			return ifelse(t);
		case LogoAST2Parser.LT:
			unhandledTypeError(t); // <
		case LogoAST2Parser.LTE:
			unhandledTypeError(t); // <=
		case LogoAST2Parser.MAKE:
			return make(t);
		case LogoAST2Parser.MINUS:
			unhandledTypeError(t); // -
		case LogoAST2Parser.MODULO:
			unhandledTypeError(t); // %
		case LogoAST2Parser.MULT:
			unhandledTypeError(t); // *
		case LogoAST2Parser.NOT:
			unhandledTypeError(t); // !
		case LogoAST2Parser.NUMBER:
			return Integer.parseInt(t.getText());
		case LogoAST2Parser.OR:
			unhandledTypeError(t); // ||
		case LogoAST2Parser.PLUS:
			unhandledTypeError(t); // +
		case LogoAST2Parser.PRINT:
			return print(t);
		case LogoAST2Parser.WHILE:
			return while_(t);
		default:
			unhandledTypeError(t);
		}

		return null;
	}

	private void unhandledTypeError(Tree t) {
		log.severe("Encountered unhandled type " + t.getType() + " ("
				+ LogoAST2Parser.tokenNames[t.getType()] + ")" + " at \""
				+ t.getText() + "\"" + " on line " + t.getLine() + ":"
				+ t.getCharPositionInLine());
		System.exit(1);
	}

	private String id(Tree t) {
		return t.getText();
	}

	private Object name(Tree t) {
		String variableName = (String) exec(t.getChild(0));
		log.info("Encountered variable or string " + variableName);
		return variableName;
	}

	private Object val(Tree t) {
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

		return 0;

	}

	Object ifelse(Tree t) {
		Tree condition = t.getChild(0);
		Tree iftrue = t.getChild(1);
		Tree iffalse = t.getChild(2);

		Object result = exec(condition);

		// Note that false what we test for
		if (FALSE.equals(result) || ZERO.equals(result)) {
			exec(iffalse);
		} else {
			exec(iftrue);
		}

		return null;
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

		return 0;

	}

	Object unary(Tree t) {

		return 0;

	}

	Object mult(Tree t) {

		return 0;

	}

	Object modulu(Tree t) {

		return 0;

	}

	Object add(Tree t) {

		return 0;
		
	}

	Object equality(Tree t) {

		return 0;

	}

	Object expression(Tree t) {

		return 0;

	}

}