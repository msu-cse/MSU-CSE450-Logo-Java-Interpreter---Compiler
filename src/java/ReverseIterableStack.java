import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;

/**
 * Same as a regular {@link Stack}, but iterator operations start and the
 * end (top) and work toward the beginning (bottom).
 * @author zach
 *
 * @param <T> Stack type
 */
public class ReverseIterableStack<T> extends java.util.Stack<T> implements
		Iterable<T> {
	private static final long serialVersionUID = -109740680077359957L;

	public class StackIterator implements Iterator<T> {
		ReverseIterableStack<T> stack;
		T current;

		public StackIterator(ReverseIterableStack<T> stack) {
			this.stack = stack;
			this.current = stack.get( stack.size() - 1 );
		}

		private void checkSanity() {
			if (!stack.contains(current))
				throw new ConcurrentModificationException();
		}

		@Override
		public boolean hasNext() {
			checkSanity();

			if (stack.indexOf(current) > 0)
				return true;

			return false;
		}

		@Override
		public T next() {
			checkSanity();

			current = stack.get(stack.indexOf(current));

			return current;
		}

		@Override
		public void remove() {
			checkSanity();

			T old = current;

			current = next();

			stack.remove(old);
		}

	}

	@Override
	public Iterator<T> iterator() {
		return new StackIterator(this);
	}
}