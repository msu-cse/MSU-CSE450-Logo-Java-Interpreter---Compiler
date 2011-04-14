import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class ArgumentTypeList extends ArrayList<Type> {
	public ArgumentTypeList(String args) {
		CharacterIterator it = new StringCharacterIterator(args);
		
		for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
		    switch(ch) {
		    case 'I': add(Type.INT); break;
		    case 'F': add(Type.FLOAT); break;
		    case 'D': add(Type.DOUBLE); break;
		    case 'Z': add(Type.BOOLEAN); break;
		    }
		}
	}
	
	public String toString() {
		String s = new String();
		for(Type t : this) {
			s += t.primitive;
		}
		return s;
	}
}
