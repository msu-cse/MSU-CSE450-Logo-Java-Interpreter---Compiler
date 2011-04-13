import org.antlr.runtime.Token;

public class FloatNode extends ScopedTree {
	public FloatNode() {
		super();
		this.valueType = Type.FLOAT;
	}

	public FloatNode(Token payload) {
		super(payload);
		this.valueType = Type.FLOAT;
	}

	Float getValue() {
		return Float.parseFloat(this.getText());
	}

	public String toString() {
		return this.getText()+"f";
	}
}
