import java.util.Iterator;

import org.antlr.runtime.tree.Tree;

/**
 * Implements the {@link Iterable} pattern for {@link Tree}.
 * 
 * @note This class would normally be in a completely separate file, but we can
 *       only turn in specific files for the project.
 * 
 * @author riggleza
 */
class IterableTree implements Iterable<Tree>, Iterator<Tree> {

	Tree t;
	Integer current = -1;

	public IterableTree(Tree t) {
		this.t = t;
	}

	@Override
	public Iterator<Tree> iterator() {
		return new IterableTree(t);
	}

	@Override
	public boolean hasNext() {
		return (current + 1) < t.getChildCount();
	}

	@Override
	public Tree next() {
		return t.getChild(++current);
	}

	@Override
	public void remove() {
		t.deleteChild(current--);
	}


}