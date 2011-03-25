import org.antlr.runtime.tree.Tree;

public class LogoException extends RuntimeException {
	public LogoException(String message) {
		super(message);
	}
	
	public LogoException(Tree t, String message) {
		super(message + " at " + t.toString() + " on line:index " + t.getLine() + ":" + t.getCharPositionInLine()
				+ "\n" + t.getParent());
	}
}
