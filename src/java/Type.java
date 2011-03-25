import org.antlr.runtime.tree.Tree;

import com.sun.corba.se.impl.io.TypeMismatchException;


public class Type {
	
	static final Type INT = new Type(Integer.class);
	static final Type FLOAT = new Type(Float.class);
	static final Type STRING = new Type(String.class);
	
	@SuppressWarnings("rawtypes")
	Class type;
	
	@SuppressWarnings("rawtypes")
	private Type(Class type) {
		this.type = type;
	}
	
	public static Type fromObject(Object o) {
		if(o instanceof Integer) return INT;
		if(o instanceof Float)   return FLOAT;
		if(o instanceof String)	 return STRING;
		
		throw new TypeMismatchException("Object " + o.toString() + " is not of a valid type");
	}
	
	public static Type getResultType(Type left, ScopedTree right) {	
		return getResultType(left, right.valueType);
	}
		
	/**
	 * Given an operation, e.g. x + y, modulo x y, etc., determine the resulting
	 * type.  Note that 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Type getResultType(Type left, Type right) {
		if(left == null)	throw new RuntimeException("Left operand is null");
		if(right == null)	throw new RuntimeException("Right operand is null");
		
		
		if(left.equals(FLOAT) && isNumber(right))
			return FLOAT;
		
		if(right.equals(FLOAT) && isNumber(left))
			return FLOAT;
		
		if(isNumber(right) && isNumber(left))
			return INT;
		
		if(isString(right) && isString(left))
			return STRING;
		
		throw new UnsupportedOperationException("Cannot determine what to do with " + left + " and " + right);
	}
	
	public static boolean isString(Type t) {
		return t.type.equals(String.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean isNumber(Type t) {
		return t.equals(INT) || t.equals(FLOAT);
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
			return "int";
		if(this.equals(FLOAT))
			return "float";
		if(this.equals(STRING))
			return "string";
		return "";
	}
	
	static Type resolve(TypeSet ts, Tree root) throws LogoException {
		if(ts.contains(STRING)
				&& !ts.contains(FLOAT)
				&& !ts.contains(INT))
			return STRING;
		
		if(ts.contains(FLOAT))
			return FLOAT;
		
		if(ts.contains(INT))
			return INT;
		
		throw new LogoException(root, "Could not determine operation result type");
	}
}
