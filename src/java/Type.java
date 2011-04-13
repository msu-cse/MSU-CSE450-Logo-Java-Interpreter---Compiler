import java.util.logging.Logger;

import org.antlr.runtime.Token;


public class Type {
	
	static final Type INT = new Type(Integer.class,"Ljava/lang/Integer","I");
	static final Type FLOAT = new Type(Float.class,"Ljava/lang/Float","F");
	static final Type STRING = new Type(String.class,"Ljava/lang/String",null);
	static final Type BOOLEAN = new Type(Boolean.class,"Ljava/lang/Boolean","Z");

	
	static Logger log = Logger.getLogger("Type"); 

	static public class LogoTypeMismatchException extends RuntimeException {
		public LogoTypeMismatchException(Token op, Type unaryType) {
			super("Attempted to perform " + op.getText()
					+ " on incompatible operand of type [" + unaryType + "]"
					+ location(op));
		}

		public LogoTypeMismatchException(Token op, Type leftType, Type rightType) {
			super("Attempted to perform " + op.getText()
					+ " on incompatible operands of types [" + leftType + ", "
					+ rightType + "]" + location(op));
		}
		
		private static String location(Token t) {
			return "at " + t.getLine() +":"+ t.getCharPositionInLine();
		}
	}
	
	@SuppressWarnings("rawtypes")
	Class type;
	
	String descriptor;
	String primitive;
	
	@SuppressWarnings("rawtypes")
	private Type(Class type,String descriptor, String primitive) {
		this.type = type;
		this.descriptor=descriptor;
		this.primitive=primitive;
	}
	
	/**
	 * Given an object, determine its {@link Type}.
	 * @param o
	 * @return
	 */
	public static Type fromObject(Object o) {
		if(o instanceof Integer) return INT;
		if(o instanceof Float)   return FLOAT;
		if(o instanceof String)	 return STRING;
		if(o instanceof Boolean) return BOOLEAN;
		if(o instanceof Symbol)  return ((Symbol)o).getValueType();
		
		throw new LogoException("Object " + o.toString() + " is not of a valid type");
	}
		
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		
		if(obj instanceof Type) {
			return this.type.equals( ((Type) obj).type );
		} else if (obj instanceof Class) {
			return this.type.equals(obj);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		if(this.equals(INT))
			return "i";
		if(this.equals(FLOAT))
			return "f";
		if(this.equals(STRING))
			return "s";
		if(this.equals(BOOLEAN))
			return "b";

		return "<unknown type>";
	}
	
	static Type resolve(Token unaryOp, Type unaryType) {
		return unaryType;
	}
	
	static Type resolveBool(Token op, Type l, Type r) {
		if(!l.equals(r))
			throw new LogoTypeMismatchException(op,l,r);
		return l;
	}
	
	static Type resolveMath(Type l, Type r) {
		return resolveMath(null,l,r);
	}
	
	static boolean coerce(Type l, Type r) {
		if(coerceRight(l,r) || coerceLeft(l,r)) {
			return true;
		}
		return false;
	}
	
	static boolean coerceRight(Type l, Type r) {
		if(l == FLOAT && r == INT)
			return true;
		return false;
	}
	
	static boolean coerceLeft(Type l, Type r) {
		if(l == INT && r == FLOAT)
			return true;
		return false;
	}
	
	static Type resolveMath(Token op, Type l, Type r) {
		log.info("Resolving " + op + ": " + l + ", " + r);
		
		if (l == STRING || l == BOOLEAN || r == STRING || r == BOOLEAN)
			throw new LogoTypeMismatchException(op, l, r);

		if (l == FLOAT || r == FLOAT)
			return FLOAT;
		
		return INT;
	}
	
	static boolean isFloat(Type l, Type r) {
		return resolveMath(l, r).equals( FLOAT );
	}
	
}
