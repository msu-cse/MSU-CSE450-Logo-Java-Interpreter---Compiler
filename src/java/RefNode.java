import org.antlr.runtime.Token;


public class RefNode extends ScopedTree {
	public RefNode() {
		super();
	}
	public RefNode(Token payload) {
		super(payload);
	}
	
//	public String toString() {
//		return "ref " + this.getText();
//	}
}
