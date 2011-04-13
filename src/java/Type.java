import java.util.logging.Logger;

import org.antlr.runtime.Token;


public class Type {
	
	static final Type INT = new Type(Integer.class,"Ljava/lang/Integer","I");
	static final Type FLOAT = new Type(Float.class,"Ljava/lang/Float","F");
	static final Type STRING = new Type(String.class,"Ljava/lang/String",null);
	static final Type BOOLEAN = new Type(Boolean.class,"Ljava/lang/Boolean","Z");
	
	/**
	 * Returns the Java descriptor that represents an instance of this type.
	 * @return
	 */
	String getDescriptor() {
		if(type == Integer.class)
			return "Ljava/lang/Integer";
		if(type == Float.class)
			return "Ljava/lang/Float";
		if(type == String.class)
			return "Ljava/lang/String";
		if(type == Boolean.class)
			return "Ljava/lang/Boolean";
		
		throw new RuntimeException("Attempted to get class descriptor for unhandled type" + this);
	}

	
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
	
	/** 
	 * Helper to resolve the return type of two-part operations AND catch
	 * invalid operations without massive duplication of work.
	 * 
	 * @param left
	 * @param right
	 * @param root
	 * @return
	 */
	static Type resolve(Object left, Object right, ScopedTree root) {
		TypeSet ts = new TypeSet();
		ts.add( fromObject(left) );
		ts.add( fromObject(right) );
		return resolve(ts, root);
	}
	
	/**
	 * Helper to resolve the return type of unary ops.
	 * 
	 * The only unary op we really have is NOT, but hey.
	 * 
	 * @param ts
	 * @param root
	 * @return
	 * @throws LogoException
	 * @see {@link #resolve(Object, Object, ScopedTree)}
	 */
	static Type resolve(Object unary, ScopedTree root) {
		TypeSet ts = new TypeSet();
		ts.add( fromObject(unary) );
		return resolve(ts, root);
	}
	
	/**
	 * Given a set of operand types, and a single root node/operation type,
	 * determine if the operation is valid.
	 * 
	 * The choice to use a {@link TypeSet} is because it's non-trivial to
	 * implement recursive grammar rules that are aware of the non-recursive 
	 * part.  Using a set simplifies this, and simplifies the logic.
	 * {@link TypeSet#contains(Object)} is much simpler than lots of
	 * <code>&&</code>
	 * 
	 * @param ts
	 * @param root
	 * @return
	 * @throws LogoException
	 */
	static Type resolve(TypeSet ts, ScopedTree root) throws LogoException {
		
		log.info("Resolving " + root.getTypeText() + " over " + ts.toString());
		
		// -- Ignore all nulls, which are unresolvable variables.
		// Decisions involving variables will have to be made at runtime, once
		// the variable can be resolved.
		ts.remove(null);
		
		// -- Is there anything?  If the set is empty, it's likely just a single
		// variable, e.g. "print :x"
		if(ts.size() == 0) {
			return null;
		}
		
		// -- Incompatible expressions first.
		// Strings can only operate with strings, e.g. "x + 7
		if(ts.contains(STRING) && 
				(ts.contains(INT)
				|| ts.contains(FLOAT)
				|| ts.contains(BOOLEAN)) ) {
			ts.remove(STRING);
			throw new LogoException(root, "Cannot perform operations with Strings and other types: " + ts);
		}
		
		// -- Impossible String operations
		// e.g. "x <= "z
		// (Note: Don't misinterpret that as :x <= :z)
		if(ts.contains(STRING)) {
			switch(root.getType()) {
				case LogoJVM1Parser.AND:
				case LogoJVM1Parser.OR:
				case LogoJVM1Parser.NOT:
				case LogoJVM1Parser.GT:
				case LogoJVM1Parser.GTE:
				case LogoJVM1Parser.LT:
				case LogoJVM1Parser.LTE:
				case LogoJVM1Parser.MINUS:
				case LogoJVM1Parser.MULT:
				case LogoJVM1Parser.DIV:
				case LogoJVM1Parser.MODULO:
					throw new LogoException(root, "Illegal operation attempted on String: " + root.getTypeText());
			}
			
			// -- Any other possible String operations (PLUS is the only one)
			// will return a String.
			return STRING;
		}
		
		// -- Impossible Boolean operations
		// e.g. (1 <= 2) / 7
		if(ts.contains(BOOLEAN)) {
			switch(root.getType()) {
			case LogoJVM1Parser.GT:
			case LogoJVM1Parser.GTE:
			case LogoJVM1Parser.LT:
			case LogoJVM1Parser.LTE:
			case LogoJVM1Parser.PLUS:
			case LogoJVM1Parser.MINUS:
			case LogoJVM1Parser.MULT:
			case LogoJVM1Parser.DIV:
			case LogoJVM1Parser.MODULO:
				throw new LogoException(root, "Illegal operation attempted on Boolean: " + root.getTypeText());
			}
		}
		
		// -- If we are performing a number-compatible operation
		// We've verified...
		// ...strings are not involved (7 + "x)
		// ...we are not performing arithmetic against a bool (7 + (true))
		// ...we are not performing comparison against a bool (7 < (true))
		//
		// So we know it's a *valid* operation, we just need to know the return
		// type.
		switch (root.getType()) {
		case LogoJVM1Parser.AND:
		case LogoJVM1Parser.OR:
		case LogoJVM1Parser.NOT:
		case LogoJVM1Parser.GT:
		case LogoJVM1Parser.GTE:
		case LogoJVM1Parser.LT:
		case LogoJVM1Parser.LTE:
		case LogoJVM1Parser.EQ:
			return BOOLEAN;
		}
			
		// -- If we make it here, we are performing an arithmetic operation
		// that is only against numbers.  Note that FLOAT takes precedence.
		if(ts.contains(FLOAT)) {
			
			if(ts.contains(INT)) {
				// XXX: Hard-coded warning, per Project 4.
				// Normally this would get sent to StdErr, but anyhoo...
				System.out
						.println("WARNING: Implicit cast from INT to FLOAT in arithmetic operation "
								+ root.getParent().getText()
								+ " at "
								+ root.getParent().fileLocation());
			}

			return FLOAT;
		}
		else if(ts.contains(INT))
			return INT;
		
		throw new LogoException(root, "Could not determine operation result type");
	}
	
}
