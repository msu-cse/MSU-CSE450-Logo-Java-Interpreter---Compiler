public class ScopeStack extends ReverseIterableStack<Scope> {
	private static final long serialVersionUID = -7827547914640832282L;

	private static ScopeStack stack = new ScopeStack();

	public static ScopeStack getInstance() {
		return stack;
	}

	public Symbol get(String symbol) {
		for (Scope scope : this) {
			if (scope.memorySpace.containsKey(symbol))
				return scope.memorySpace.get(symbol);
		}

		return null;
	}

	public void put(String symbolName, Symbol symbol) {
		lastElement().memorySpace.put(symbolName, symbol);
	}
}