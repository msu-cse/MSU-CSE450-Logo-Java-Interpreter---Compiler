import java.util.HashSet;
import java.util.TreeSet;

import org.antlr.runtime.tree.Tree;


public class TypeSet extends HashSet<Type> {
	public boolean add(ScopedTree e) throws LogoException {
		if(e == null)
			return false;
		
		if(e.valueType == null) 
			throw new LogoException(e, "Cannot resolve type");
		
		boolean retVal = super.add(e.valueType);
		
		// See if everything resolves properly.  If it doesn't throw 
		// an exception, it's OK.
		try {
			Type.resolve(this, e);
		} catch (LogoException ex) {
			throw new LogoException(e,"Invalid operation");
		}
		
		return retVal;
	}
}
