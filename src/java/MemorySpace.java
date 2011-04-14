import java.util.HashMap;
import java.util.Stack;

public class MemorySpace extends HashMap<String, Symbol> {
	private static final long serialVersionUID = 5326929418488753186L;
	public static Stack scopeStack;

	// XXX: This is a hack to start at 1. Really, we need to start at
	// number-of-args. Given that main() has one arg, we start at one.
	// XXX: This is a hack to use a static.  Just doing this to get at the count
	// from within the grammar easily for Project 5.
	static Integer nextIndex = 1;

	Symbol create(String key) {
		Symbol s;
		if (!containsKey(key)) {
			s = new Symbol(key, nextIndex);
			put(key, s);
			nextIndex += 1;
		} else {
			s = get(key);
		}
		return s;
	}

	Symbol find(String key) {
		// RFE 4475301 should would be nice...
		// Unfortunately, to iterate top-down through a stack, you must iterate
		// backward.
		for (Integer i = scopeStack.size() - 1; i >= 0; i--) {
			MemorySpace ms = (MemorySpace) scopeStack.get(i);
			if (ms.containsKey(key))
				return ms.get(key);
		}
		return null;
	}

}
