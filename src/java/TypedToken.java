import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

public class TypedToken extends CommonToken {

	Type valueType;
	
	public TypedToken(Token oldToken) {
		super(oldToken);
	}

	public TypedToken(int type, String text) {
		super(type, text);
	}

	public TypedToken(int type) {
		super(type);
	}

	public TypedToken(CharStream input, int type, int channel, int start,
			int stop) {
		super(input, type, channel, start, stop);
	}

}
