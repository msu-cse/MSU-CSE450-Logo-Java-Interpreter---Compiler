
public class Symbol {
	String name;
	Object value;
	
	public Symbol(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "[" + name + " = " + value + "]";
	}
}
