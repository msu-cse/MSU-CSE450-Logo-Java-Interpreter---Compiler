import java.util.Iterator;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


public class TypeAwareTree  extends CommonTree implements Iterable<TypeAwareTree> {
	public TypeAwareTree(Token payload) {
		super(payload);
	}

	ReturnType type;
	
	private class TypeAwareTreeIterator implements Iterator<TypeAwareTree> {
		TypeAwareTree t;
		Integer current = -1;

		TypeAwareTreeIterator(TypeAwareTree t) {
			this.t = t;
		}
		
		@Override
		public boolean hasNext() {
			return (current + 1) < t.getChildCount();
		}

		@Override
		public TypeAwareTree next() {
			// getChild is not guaranteed to be a CommonTree. Hope. Just hope.
			return (TypeAwareTree) t.getChild(++current);
		}

		@Override
		public void remove() {
			t.deleteChild(current--);
		}
	}

	@Override
	public Iterator<TypeAwareTree> iterator() {
		return new TypeAwareTreeIterator(this);
	}
	
	@Override
	public TypeAwareTree getChild(int i) {
		return (TypeAwareTree) super.getChild(i);
	}

}
