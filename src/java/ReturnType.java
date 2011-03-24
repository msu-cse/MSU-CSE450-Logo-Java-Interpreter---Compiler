import com.sun.corba.se.impl.io.TypeMismatchException;


public class ReturnType {
	
	static final ReturnType INT = new ReturnType(Integer.class);
	static final ReturnType FLOAT = new ReturnType(Float.class);
	static final ReturnType STRING = new ReturnType(String.class);
	
	@SuppressWarnings("rawtypes")
	Class type;
	
	@SuppressWarnings("rawtypes")
	private ReturnType(Class type) {
		this.type = type;
	}
	
	public static ReturnType fromObject(Object o) {
		if(o instanceof Integer) return INT;
		if(o instanceof Float)   return FLOAT;
		if(o instanceof String)	 return STRING;
		
		throw new TypeMismatchException("Object " + o.toString() + " is not of a valid type");
	}
	
	/**
	 * Given an operation, e.g. x + y, modulo x y, etc., determine the resulting
	 * type.  Note that 
	 * @param left
	 * @param right
	 * @return
	 */
	public static ReturnType getResultType(ReturnType left, ReturnType right) {
		if(left.equals(FLOAT) && isNumber(right))
			return FLOAT;
		
		if(right.equals(FLOAT) && isNumber(left))
			return FLOAT;
		
		if(isNumber(right) && isNumber(left))
			return INT;
		
		if(isString(right) && isString(left))
			return STRING;
		
		throw new TypeMismatchException();
	}
	
	public static boolean isString(ReturnType t) {
		return t.type.equals(String.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean isNumber(ReturnType t) {
		return t.type.isAssignableFrom(Number.class);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReturnType) {
			return this.type.equals( ((ReturnType) obj).type );
		}
		
		return super.equals(obj);
	}
}
