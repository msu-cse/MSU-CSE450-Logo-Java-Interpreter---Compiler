import java.util.Iterator;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


public class TypeAwareTree  extends IterableTree<TypeAwareTree> {
	public TypeAwareTree(Token payload) {
		super(payload);
	}

	ReturnType valueType;
}
