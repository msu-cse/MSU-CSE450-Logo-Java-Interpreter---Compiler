import java.util.HashSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.antlr.runtime.tree.Tree;

public class TypeSet extends HashSet<Type> {

	Type returnType = null;

	static Logger log = Logger.getLogger("TypeSet");

	public TypeSet() {
	}

	public boolean add(ScopedTree e) throws LogoException {

		if (e == null) {
			log.info("Attempted to add null!");
			return false;
		}

		if (e.getValueType() == null) {
			log.info("Attmepted to add " + e + " with null type!");
			return false;
		}

		boolean retVal = super.add(e.getValueType());

		// -- If this type is new, add() returns true.
		// If it returns false, we didn't add any new types, and don't need
		// to check for type-safety again.
		if (retVal) {
			// If this doesn't throw an exception, we're good.
			returnType = Type.resolve(this, e);
		}

		return retVal;
	}

}
