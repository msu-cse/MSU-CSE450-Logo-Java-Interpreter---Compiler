import org.antlr.runtime.Token;


public class CallNode extends ScopedTree {
	
	Function calledFunction;


	public CallNode() { 
		super();
		makeScope();
	}
	public CallNode(Token t) {
		super(t);
		makeScope();
	}

	
	
}
