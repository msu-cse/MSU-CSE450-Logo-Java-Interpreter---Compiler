
public class Symbol {
	/** Node at which the symbol was initially declared */
	ScopedTree declarationNode;
	protected String name;
	protected Object value;

	private Type valueType;

	public Symbol(String name, Object value) {
		super();
		this.name = name;
		setValue(value);
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
	
	public void setValue(Object value) {
		this.value = value;
		
		if(value != null)
			this.valueType = Type.fromObject(value);
	}

	@Override
	public String toString() {
		return "[" + name + " = " + value + "]";
	}
}
