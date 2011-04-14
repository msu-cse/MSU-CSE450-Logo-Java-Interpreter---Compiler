import java.util.logging.Logger;

import msu.cse.turtlegraphics.Turtle;
import msu.cse.turtlegraphics.TurtleDisplayFrame;

public class Interpreter {

	public static final Boolean FALSE = new Boolean(false);

	// -- Boolean truths
	public static final Integer ZERO = new Integer(0);

	private static Interpreter instance = null;
	Logger log = Logger.getLogger("Interpreter");
	
	static Interpreter getInstance() {
		return instance ;
	}
	
	TurtleDisplayFrame frame = new TurtleDisplayFrame();

	Turtle turtle = new Turtle();

	public Interpreter() {
		instance = this;
		frame.setVisible(true);
		turtle.setCurrentTurtleDisplayCanvas(frame.getCurrentCanvas());
	}


	public Interpreter(ScopedTree t) {
		this();
		exec(t);		
	}

	Object block(ScopedTree t) {
		log.info("Executing block" + t.toStringTree());
		
		for (ScopedTree child : t)
			exec(child);
		
		return null;
	}

	private Object backward(ScopedTree t) {
		ScopedTree child = t.getChild(0);

		log.info("Moving backward " + t.getText());

		turtle.turtleBackward((Integer) exec(child));
		return null;
	}
	
	private Object forward(ScopedTree t) {
		ScopedTree child = t.getChild(0);

		
		Integer distance = (Integer) exec(child);
		log.info("Moving forward" + distance);

		turtle.turtleForward(distance);
		return null;
	}


	private Object beginFill(ScopedTree t) {
		log.info("Beginning fill");
		turtle.turtleBeginFillPolygon();
		return null;
	}

	private Object circle(ScopedTree t) {
		ScopedTree radius = t.getChild(0);
		ScopedTree angle = t.getChild(1);

		log.info("Drawing circle with args " + radius.getText() + ", "
				+ angle.getText());

		turtle.turtleCircle((Integer) exec(radius), (Integer) exec(angle));
		return null;
	}
	private Object endFill(ScopedTree t) {
		log.info("Ending fill");
		turtle.turtleEndFillPolygon();
		return null;
	}

	private Object end(ScopedTree t) {
		// Don't actually do anything with the end.
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
		case LogoJVM1Parser.BLOCK:
			return block(t);

		case LogoJVM1Parser.AND:
			return InterpreterLogic.op(t); // &&
		case LogoJVM1Parser.BACKWARD:
		case LogoJVM1Parser.BACKWARD2:
			return backward(t);
		case LogoJVM1Parser.BEGINFILL:
			return beginFill(t);
		case LogoJVM1Parser.BYNAME:
			return name(t);
		case LogoJVM1Parser.BYVAL:
			return val(t);
		case LogoJVM1Parser.CIRCLE:
			return circle(t);
		case LogoJVM1Parser.COLOR:
			return setPenColor(t);
		case LogoJVM1Parser.DIV:
			return InterpreterMath.op(t); // /
		case LogoJVM1Parser.END:
			return end(t);
		case LogoJVM1Parser.FLOAT:
			return Float.parseFloat(t.getText());
		case LogoJVM1Parser.ENDFILL:
			return endFill(t);
		case LogoJVM1Parser.EQ:
			return equality(t); // ==
		case LogoJVM1Parser.FORWARD:
		case LogoJVM1Parser.FORWARD2:
			return forward(t);
		case LogoJVM1Parser.GT:
			return InterpreterMath.op(t); // >
		case LogoJVM1Parser.GTE:
			return InterpreterMath.op(t); // >=
		case LogoJVM1Parser.ID:
			return id(t);
		case LogoJVM1Parser.IF:
			return if_(t);
		case LogoJVM1Parser.IFELSE:
			return ifelse(t);
		case LogoJVM1Parser.LEFT:
		case LogoJVM1Parser.LEFT2:
			return left(t);
		case LogoJVM1Parser.LT:
			return InterpreterMath.op(t); // <
		case LogoJVM1Parser.LTE:
			return InterpreterMath.op(t); // <=
		case LogoJVM1Parser.MAKE:
			return make(t);
		case LogoJVM1Parser.MINUS:
			return InterpreterMath.op(t); // -
		case LogoJVM1Parser.MODULO:
			return InterpreterMath.op(t); // %
		case LogoJVM1Parser.MULT:
			return InterpreterMath.op(t); // *
		case LogoJVM1Parser.NOT:
			return InterpreterLogic.op(t); // !
		case LogoJVM1Parser.INTEGER:
			return Integer.parseInt(t.getText());
		case LogoJVM1Parser.OR:
			return InterpreterLogic.op(t); // ||
		case LogoJVM1Parser.PENDOWN:
			return penDown(t);
		case LogoJVM1Parser.PENUP:
			return penUp(t);
		case LogoJVM1Parser.PLUS:
			return InterpreterMath.op(t); // +
		case LogoJVM1Parser.PRINT:
			return print(t);
		case LogoJVM1Parser.RETURN:
			return return_(t);
		case LogoJVM1Parser.TO:
			return to(t);
		case LogoJVM1Parser.REPEAT:
			return repeat(t);
		case LogoJVM1Parser.RIGHT:
		case LogoJVM1Parser.RIGHT2:
			return right(t);
		case LogoJVM1Parser.SETHEADING:
		case LogoJVM1Parser.SETHEADING2:
			return setHeading(t);
		case LogoJVM1Parser.WHILE:
			return while_(t);
		default:
			unhandledTypeError(t);
		}

		return null;
	}

	Object id(ScopedTree t) {
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

	private Object left(ScopedTree t) {
		ScopedTree child = t.getChild(0);

		log.info("Turning left " + t.getText());

		turtle.turtleLeft((Integer) exec(child));
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
	private Object penDown(ScopedTree t) {
		log.info("Pen Down");
		turtle.turtlePenDown();
		return null;
	}

	private Object penUp(ScopedTree t) {
		log.info("Pen Up");
		turtle.turtlePenUp();
		return null;
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

	private Object repeat(ScopedTree t) {
		Integer count = (Integer) exec(t.getChild(0));
		ScopedTree blk = t.getChild(1);

		log.info("Repeating block " + count + "times: " + blk.toStringTree());

		for (int i = 0; i < count; i++) {
			exec(blk);
		}

		return null;
	}

	private Object right(ScopedTree t) {
		ScopedTree child = t.getChild(0);

		log.info("Turning right " + t.getText());

		turtle.turtleRight((Integer) exec(child));
		return null;
	}

	private Object setHeading(ScopedTree t) {
		ScopedTree child = t.getChild(0);

		log.info("Setting heading " + t.getText());

		turtle.turtleSetHeading(((Double) exec(child)));
		return null;
	}

	private Object setPenColor(ScopedTree t) {
		Integer r = (Integer) exec(t.getChild(0));
		Integer g = (Integer) exec(t.getChild(1));
		Integer b = (Integer) exec(t.getChild(2));
		turtle.turtleSetColor(r, g, b);
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
				+ LogoJVM1Parser.tokenNames[t.getType()] + ")" + " at \""
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
