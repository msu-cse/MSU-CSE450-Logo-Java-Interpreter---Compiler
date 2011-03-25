import java.util.Iterator;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

abstract class IterableTree<T extends IterableTree<?>> extends TreeHelper implements Iterable<T> {
	
	@SuppressWarnings("hiding")
	private class IterableTreeIterator<T extends IterableTree<?>> implements Iterator<T> {
		IterableTree<T> t;
		Integer current = -1;

		public IterableTreeIterator(IterableTree<T> iterableTree) {
			if(!(iterableTree instanceof Tree)) {
				throw new UnsupportedOperationException("Class must be an instance of Tree to use " + IterableTree.class);
			}
			t = iterableTree;
		}
		
		@Override
		public boolean hasNext() {
			return (current + 1) < t.getChildCount();
		}

		@Override
		public T next() {
			// getChild is not guaranteed to be a CommonTree. Hope. Just hope.
			return (T) t.getChild(++current);
		}

		@Override
		public void remove() {
			t.deleteChild(current--);
		}
	}

	public IterableTree(Token payload) {
		super(payload);
	}

	public IterableTree() {
		super();
	}

	@Override
	public Iterator<T> iterator() {
		return new IterableTreeIterator<T>(this);
	}
}