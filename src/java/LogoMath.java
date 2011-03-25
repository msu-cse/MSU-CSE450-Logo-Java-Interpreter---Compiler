import java.util.logging.Logger;


/**
 * LOGO Math operations.
 * 
 * Refactored here from {@link Interpreter} for easy separation of functionality.
 * 
 * @author zach
 *
 */
public class LogoMath {

	static Logger log = Logger.getLogger("Math");
	
	static Number op(ScopedTree a, Object x, Object y) {
		
		Number retVal = null;
		
		if(x instanceof Float || y instanceof Float)
			retVal = floatOp(a,(Number) x, (Number) y);
		
		if(x instanceof Integer || y instanceof Integer)
			retVal = intOp(a, (Number)x, (Number) y);
		
		log.info(a.toString() + " returned " + retVal);
		
		return retVal;
	}
	
	static Float floatOp(ScopedTree t, Number a, Number b) {
		Float x = a.floatValue();
		Float y = b.floatValue();
		
		switch(t.getType()) {
			case LogoTurtleParser.PLUS:		return x+y;
			case LogoTurtleParser.MINUS:	return x-y;
			case LogoTurtleParser.DIV:		return x/y;
			case LogoTurtleParser.MULT:		return x*y;
			case LogoTurtleParser.MODULO:	return x%y;
		}
		
		throw new RuntimeException("Cannot perform math operation " + t);
	}
	
	static Integer intOp(ScopedTree t, Number a, Number b) {
		Integer x = a.intValue();
		Integer y = b.intValue();
		
		switch(t.getType()) {
			case LogoTurtleParser.PLUS:		return x+y;
			case LogoTurtleParser.MINUS:	return x-y;
			case LogoTurtleParser.DIV:		return x/y;
			case LogoTurtleParser.MULT:		return x*y;
			case LogoTurtleParser.MODULO:	return x%y;
		}		
		
		throw new RuntimeException("Cannot perform math operation " + t);
	}

	public static Object op(ScopedTree t) {
		
		if(t.getChildCount() == 2) {
			Object left = Interpreter.e(t.getChild(0));
			Object right = Interpreter.e(t.getChild(1));
			
			return op(t, left, right);
		} else if(t.getChildCount() == 1) {
			Object unary = Interpreter.e(t.getChild(0));
			return op(t, unary);
		} else {
			throw new RuntimeException("Attempted to perform math on node " + t);
		}
	}

	static Object op(ScopedTree t, Object unary) {
		return null;
	}
	
}
