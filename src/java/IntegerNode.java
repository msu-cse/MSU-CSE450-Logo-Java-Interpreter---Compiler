import org.antlr.runtime.Token;


public class IntegerNode extends ScopedTree {
	public IntegerNode() {
		super();
		this.valueType = Type.INT;
	}
	public IntegerNode(Token payload) {
		super(payload);
		this.valueType = Type.INT;
	}
	
	Integer getValue() {
		return Integer.parseInt(this.getText());
	}

	public String toString() {
		return this.getText()+"i";
	}
}
