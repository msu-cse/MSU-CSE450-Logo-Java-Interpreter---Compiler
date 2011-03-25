import java.util.logging.Logger;


public class LogoLogic {

	static Logger log = Logger.getLogger("Boolean");
	
	static Boolean toBool(Object o) {
		if(o instanceof Float)
			return ((Float)o).equals(0) ? false : true;
		
		if(o instanceof Integer)
			return ((Integer)o).equals(0) ? false : true;
		
		if(o instanceof String)
			return ((String)o).length() <= 0 ? false : true;
		
		if(o instanceof Boolean)
			return (Boolean) o;
		
		throw new LogoException("Cannot convert " + o + " to Boolean");
	}
		
	static Boolean boolBinaryOp(ScopedTree t, Boolean x, Boolean y) {
		log.info ("Values: " + x + ", " + y);
		
		switch(t.getType()) {
			case LogoTurtleParser.AND:		return x && y;
			case LogoTurtleParser.OR:		return x || y;
		}
		throw new RuntimeException("Cannot perform boolean operation " + t);
	}
	
	static Boolean boolUnaryOp(ScopedTree t, Boolean x) {
		log.info ("Value: " + x);

		switch (t.getType()) {
		case LogoTurtleParser.NOT:			return !x;
		}
		throw new RuntimeException("Cannot perform boolean operation " + t);
	}

	public static Object op(ScopedTree t) {
		log.info("Evaluating " + t.toStringTree());
		
		Boolean returnValue = null;
		
		if (t.getChildCount() == 2) {
			Boolean left = toBool(Interpreter.e(t.getChild(0)));
			Boolean right = toBool(Interpreter.e(t.getChild(1)));

			returnValue = boolBinaryOp(t, left, right);
		} else if (t.getChildCount() == 1) {
			Boolean unary = toBool(Interpreter.e(t.getChild(0)));
			returnValue = boolUnaryOp(t, unary);
		}
		
		if(returnValue != null) {
			log.info("Got " + returnValue);
			return returnValue;
		}
			

		throw new RuntimeException("Attempted to perform math on node " + t);
	}
}
