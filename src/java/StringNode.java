import org.antlr.runtime.Token;

public class StringNode extends ScopedTree {
	public StringNode() {
		super();
	}

	public StringNode(Token payload) {
		super(payload);
	}

	String getValue() {
		return this.getText();
	}

//	public String toString() {
//		return "string " + this.getText();
//	}
}
