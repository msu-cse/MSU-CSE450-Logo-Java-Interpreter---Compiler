import java.util.HashMap;

public class MemorySpace extends HashMap<String, Integer> {
	private static final long serialVersionUID = 5326929418488753186L;

	Integer nextIndex = 0;

	void generateVariableId(String key) {
		if (!containsKey(key)) {
			put(key, nextIndex);
			nextIndex += 1;
		}
	}
}
