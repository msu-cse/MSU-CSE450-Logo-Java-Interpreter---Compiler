import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class LogoASTAdaptor extends CommonTreeAdaptor {
	@Override
	public Object create(Token payload) {
		return new TypeAwareTree(payload);
	}

	public Object dupNode(Object t) {
		if (t == null) {
			return null;
		}
		return create(((TypeAwareTree) t).token);
	}
}
