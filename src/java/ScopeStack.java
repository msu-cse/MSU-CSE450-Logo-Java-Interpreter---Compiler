import java.util.logging.Logger;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;


/**
 * Stack of all scopes.  Created and instantiated as needed.
 * @author zach
 *
 */
public class ScopeStack extends ReverseIterableStack<Scope> {
	private static final long serialVersionUID = -7827547914640832282L;

	private static ScopeStack stack = new ScopeStack();
	
	public ScopeStack() {
		push(new Scope(null));
	}

	public static ScopeStack getInstance() {
		return stack;
	}
	Logger log = Logger.getLogger("ScopeStack");

	@Override
	public Scope push(Scope item) {
		if(item == null) throw new NullPointerException();
		log.info("Creating scope for " + item);
		return super.push(item);
	}
	
	@Override
	public synchronized Scope pop() {
		Scope x = super.pop();
		log.info("Popping scope for " + x);
		return x;
	}

	public Symbol get(String symbol) {
		for (Scope scope : this) {
			if (scope.memorySpace.containsKey(symbol))
				return scope.memorySpace.get(symbol);
		}

		return null;
	}

	public void put(String name, Object value) {
		Symbol symbol = new Symbol(name,value);
		log.info("Pushing " + symbol + " into scope " + lastElement());
		
		if(name == null) throw new NullPointerException();
		if(value == null) throw new NullPointerException();
		
		lastElement().memorySpace.put(symbol.name, symbol);
	}
}