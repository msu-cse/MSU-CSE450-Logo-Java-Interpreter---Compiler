import java.util.HashSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.antlr.runtime.tree.Tree;


public class TypeSet extends HashSet<Type> {
	
	Type returnType = null;
	
	static Logger log = Logger.getLogger("TypeSet");

	
	public boolean add(ScopedTree e) throws LogoException {
		log.info(e.toStringTree());
		
		if(e == null)
			return false;
				
		if(e.valueType == null)  {
			return false;
		}
						
		boolean retVal = super.add(e.valueType);
		
		// -- If this type is new, add() returns true.
		// If it returns false, we didn't add any new types, and don't need
		// to check for type-safety again.
		if(retVal) {
			// If this doesn't throw an exception, we're good.
			log.info("Added '" + e.valueType + "' to "+e.info()+", now " + this);
			returnType = Type.resolve(this, e);
		}
		
		log.info(this + " returns " + returnType);

		return retVal;
	}
}
