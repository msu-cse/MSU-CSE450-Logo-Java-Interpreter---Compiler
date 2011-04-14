import java.util.HashMap;
import java.util.logging.Logger;

import org.antlr.runtime.tree.Tree;

class TokenMap<T> extends HashMap<Integer, T> {
	Logger log = Logger.getLogger("TokenMap");

	T get(Tree t) {
		log.info("Getting " + t.toString());
		
		return get(t.getType());
	}
}
