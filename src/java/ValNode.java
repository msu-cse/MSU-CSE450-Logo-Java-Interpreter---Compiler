import org.antlr.runtime.Token;


public class ValNode extends ScopedTree {
	public ValNode() {
		super();
	}
	public ValNode(Token payload) {
		super(payload);
	}
	
	public String toString() {
		return this.getText();
	}
}
