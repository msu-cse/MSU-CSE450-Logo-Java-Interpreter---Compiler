import org.antlr.runtime.Token;

public class FloatNode extends ScopedTree {
	public FloatNode() {
		super();
	}

	public FloatNode(Token payload) {
		super(payload);
	}

	Float getValue() {
		return Float.parseFloat(this.getText());
	}

	public String toString() {
		return "float " + this.getText();
	}
}
