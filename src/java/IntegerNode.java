import org.antlr.runtime.Token;


public class IntegerNode extends ScopedTree {
	public IntegerNode() {
		super();
	}
	public IntegerNode(Token payload) {
		super(payload);
	}
	
	Integer getValue() {
		return Integer.parseInt(this.getText());
	}

	public String toString() {
		return "int " + this.getText();
	}
}
