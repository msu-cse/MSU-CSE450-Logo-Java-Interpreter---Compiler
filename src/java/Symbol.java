public class Symbol {
	/** Node at which the symbol was initially declared */
	ScopedTree declarationNode;
	
	/** 
	 * Integer ID for the symbol 
	 *
	 * Only used for bytecode generation
	 */
	protected Integer id;
	
	/** Original name for the symbol */
	protected String name;
	
	/** 
	 * Symbol's value
	 * 
	 * - Only used for interpreter
	 */
	protected Object value;
	
	/** 
	 * Symbol's type
	 *
	 * - Inferred from the tree
	 * - Also determined separately at runtime
	 */
	protected Type valueType;

	public Symbol(String name, Object value) {
		super();
		this.name = name;
		setValue(value);
	}

	public Symbol(String name, Type valueType) {
		super();
		this.name = name;
		this.valueType = valueType;
	}

	public Symbol(String name, Integer id) {
		super();
		this.name = name;
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public Type getValueType() {
		return valueType;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setValueType(Type t) {
		this.valueType = t;
	}

	public void setValue(Object value) {
		this.value = value;

		if (value != null)
			this.valueType = Type.fromObject(value);
	}

	@Override
	public String toString() {
		return "[" + name + " = " + value + "]";
	}
}
