import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 * Attaches scope to the AST.
 * @author zach
 */
public class ScopedTree extends IterableTree<ScopedTree>{

	/** Expression type as determined by compile-time AST rules */
	Type 	valueType;
	
	/** Value determined at runtime */
	Object 	value;
	
	static Logger log = Logger.getLogger("ScopedTree");

	protected Map<String, Symbol> memorySpace;

	public ScopedTree() {
		super();
	}
	
	public ScopedTree(Token payload) {
		super(payload);
	}

	@Override
	public ScopedTree getParent() {
		return (ScopedTree) super.getParent();
	}

	@Override
	public ScopedTree getChild(int i) {
		return (ScopedTree) super.getChild(i);
	}
	
	public void makeScope() {
		log.info("Creating scope for " + this.toStringTree());
		memorySpace = new TreeMap<String, Symbol>();
	}

	public Symbol get(String symbol, Tree requestingNode) {
		log.info ("Searching " + this.toStringTree() + " for " + symbol);
		
		if (memorySpace != null && memorySpace.containsKey(symbol)) {
			Symbol s = memorySpace.get(symbol);
			log.info("Found " + s); 
			return s;
		} else {
			log.info("... does not exist in node's memory space");
		}

		if (getParent() != null) {
			log.info ("... searching parent");
			return getParent().get(symbol, requestingNode);
		} else {
			log.info("... no parents to check");
		}

		throw new LogoException(requestingNode,"Could not find symbol '" + symbol + "'");
	}
	
	public void createSymbol(String name) {
		put(name,null);
	}

	public void put(String name, Object value) {
		log.info("Attempting to put " + name + " = " + value);
		
		if(memorySpace == null) {
			log.info(this + " does not have a memory space, trying parent.");
			
			if(getParent() != null)
				getParent().put(name, value);
		}
		else {
			Symbol symbol = new Symbol(name,value);
			log.info("Pushing " + symbol + " into scope declared on line " + this.getLine() + " (" + this.toString() + " )");
			
			if(name == null) throw new NullPointerException();
			if(value == null) throw new NullPointerException();
			
			memorySpace.put(symbol.getName(), symbol);
		}
	}
	
	@Override
	public String toString() {
		if(valueType != null)	return super.toString() + "[" + valueType + "]";
		else 					return super.toString();
	}
}
