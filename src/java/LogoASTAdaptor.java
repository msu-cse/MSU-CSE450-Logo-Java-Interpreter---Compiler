import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class LogoASTAdaptor extends CommonTreeAdaptor {
	@Override
	public Object create(Token payload) {
		return new ScopedTree(payload);
	}

	public Object dupNode(Object t) {
		if (t == null) {
			return null;
		}
		return create(((ScopedTree) t).token);
	}

	public Object errorNode(TokenStream input, Token start, Token stop,
			RecognitionException e) {
		return new ScopedTreeErrorNode(input, start, stop, e);
	}

}
