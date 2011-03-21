import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;



/**
 * Implements a variable scope for a given block.
 * @author zach
 *
 */
public class Scope {
	@SuppressWarnings("unused")
	protected Map<String,Symbol> memorySpace;
	
	@SuppressWarnings("unused")
	protected Tree   node;	
	
	public Scope(Tree node) {
		 this.node = node;
		 this.memorySpace = new HashMap<String,Symbol>();
	}	
}