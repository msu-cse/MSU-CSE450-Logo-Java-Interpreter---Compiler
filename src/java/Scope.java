import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.antlr.runtime.tree.Tree;



/**
 * Implements a variable scope for a given block.
 * @author zach
 *
 */
public class Scope {
	@SuppressWarnings("unused")
	protected Map<String,Symbol> memorySpace = new TreeMap<String,Symbol>();
	
	@SuppressWarnings("unused")
	protected Tree   node;
	
	Logger log = Logger.getLogger("Scope");
	
	public Scope(Tree node) {
		 this.node = node;
		 this.memorySpace = new HashMap<String,Symbol>();
	}	
	
	@Override
	public String toString() {
		if(node != null) return node.toStringTree();
		else return "<global scope>";
	}
}