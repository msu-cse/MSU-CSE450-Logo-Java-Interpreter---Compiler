import org.antlr.runtime.tree.Tree;

import sun.util.logging.resources.logging;

import com.sun.corba.se.impl.io.TypeMismatchException;


public class Type {
	
	static final Type INT = new Type(Integer.class);
	static final Type FLOAT = new Type(Float.class);
	static final Type STRING = new Type(String.class);
	static final Type BOOLEAN = new Type(Boolean.class);
	
	@SuppressWarnings("rawtypes")
	Class type;
	
	@SuppressWarnings("rawtypes")
	private Type(Class type) {
		this.type = type;
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
			return "int";
		if(this.equals(FLOAT))
			return "float";
		if(this.equals(STRING))
			return "string";
		if(this.equals(BOOLEAN))
			return "bool";

		return "<unknown type>";
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
}
