import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Function extends Symbol {

	List<String> parameters = new ArrayList();
	ScopedTree block = null;
	Object returnValue = null;
	static Logger log = Logger.getLogger("Function");

	public Function(String name, Object value) {
		super(name, value);
	}

	public Function(String name, ScopedTree declarationNode) {
		// XXX: Fix this, too much indirection.
		super(name, declarationNode);
	}

	public Function(ScopedTree t) {
		this(t.getChild(0).getText(), null);
		value = this;
		
		log.info("Instantiating function from " + t.info());
		
		List<ScopedTree> children = t.getChildren();
		log.info("Chidlren: " + children);
		
		// Remove the name.
		children.remove(0);
		
		// Remove the block
		block = (ScopedTree) children.remove(children.size() - 1);
		log.info("Block: " + block);
		
		for(ScopedTree child : children) {
			
			log.info("Arg: " + child);
			parameters.add(child.getChild(0).getText());
			log.info("Args: " + parameters);
		}
	}

	void call(ScopedTree caller, List arguments) {

		// Arguments should be a set of refs, e.g. :a :b :c
		if (parameters != null) {

			// Argument length is good?
			assert parameters.size() == arguments.size() : "Argument count ("
					+ arguments.size() + ")+ does not match expected count ("
					+ parameters.size() + ")";


			Iterator<ScopedTree> argIter = arguments.iterator(); 
			Iterator<String> paramIter = parameters.iterator();
				
			while(paramIter.hasNext() && argIter.hasNext()) {
//				caller.put(paramIter.next(), caller.get(argIter.get));
			}
			
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append('(');
		
		for(String param : parameters) {
			sb.append(param);
			sb.append(" ");
		}
		
		sb.append(')');
		return sb.toString();
	}
}
