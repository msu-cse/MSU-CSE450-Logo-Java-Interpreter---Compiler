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
	
	static Object binary(ScopedTree a, Object x, Object y) {
		
		Object retVal = null;
		
		log.info("Performing operation " + a.toStringTree());
		log.info("Types: " + x.getClass() + ", " + y.getClass());
		
		if(x instanceof Symbol)
			x = ((Symbol) x).getValue();
		
		if(y instanceof Symbol)
			y= ((Symbol) y).getValue();
		
		if(x instanceof Float || y instanceof Float)
			retVal = floatOp(a, (Number) x, (Number) y);
		
		else if(x instanceof Integer || y instanceof Integer)
			retVal = intOp(a, (Number)x, (Number) y);
		
		else if(x instanceof String && y instanceof String && a.getType() == LogoTurtleParser.PLUS)
			return (String) x + (String) y;
		
		log.info(a.toString() + " returned " + retVal);
		
		return retVal;
	}
	
	static Object floatOp(ScopedTree t, Number a, Number b) {
		Float x = a.floatValue();
		Float y = b.floatValue();
		
		log.info ("Values: " + x + ", " + y);
		
		switch(t.getType()) {
			case LogoTurtleParser.PLUS:		return x+y;
			case LogoTurtleParser.MINUS:	return x-y;
			case LogoTurtleParser.DIV:		return x/y;
			case LogoTurtleParser.MULT:		return x*y;
			case LogoTurtleParser.MODULO:	return x%y;
			case LogoTurtleParser.LT:		return x<y;
			case LogoTurtleParser.LTE:		return x<=y;
			case LogoTurtleParser.GT:		return x>y;
			case LogoTurtleParser.GTE:		return x>=y;
		}
		
		throw new RuntimeException("Cannot perform math operation " + t);
	}
	
	static Object intOp(ScopedTree t, Number a, Number b) {
		Integer x = a.intValue();
		Integer y = b.intValue();
		
		log.info ("Values: " + x + ", " + y);
		
		switch(t.getType()) {
			case LogoTurtleParser.PLUS:		return x+y;
			case LogoTurtleParser.MINUS:	return x-y;
			case LogoTurtleParser.DIV:		return x/y;
			case LogoTurtleParser.MULT:		return x*y;
			case LogoTurtleParser.MODULO:	return x%y;
			case LogoTurtleParser.LT:		return x<y;
			case LogoTurtleParser.LTE:		return x<=y;
			case LogoTurtleParser.GT:		return x>y;
			case LogoTurtleParser.GTE:		return x>=y;
		}		
		
		throw new RuntimeException("Cannot perform math operation " + t);
	}

	public static Object op(ScopedTree t) {
		
		if(t.getChildCount() == 2) {
			Object left = Interpreter.e(t.getChild(0));
			Object right = Interpreter.e(t.getChild(1));
			
			return binary(t, left, right);
		} else if(t.getChildCount() == 1) {
			Object unary = Interpreter.e(t.getChild(0));
			return unary(t, unary);
		}
		throw new RuntimeException("Attempted to perform math on node: "
				+ t.getTypeText());

	}

	static Object unary(ScopedTree t, Object unary) {
		throw new RuntimeException("Not implemented");
	}
	
}
