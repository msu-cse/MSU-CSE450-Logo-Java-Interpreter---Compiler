import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 * Utility class put between CommonTree and our Tree implementations to provide
 * helper functions.
 * 
 * @author zach
 * 
 */
abstract public class TreeHelper extends CommonTree {

	public TreeHelper() {
		super();
	}

	public TreeHelper(Token payload) {
		super(payload);
	}

	public String getTypeText() {
		if (getType() >= 0)
			return LogoJVM1Parser.tokenNames[getType()];
		return "<unknown type: " + getType() + ">";
	}

	public String fileLocation() {
		return + getLine() + ":" + getCharPositionInLine();
	}

	public String info() {
		return "'" + toStringTree() + "' at line " + fileLocation();
	}
}
