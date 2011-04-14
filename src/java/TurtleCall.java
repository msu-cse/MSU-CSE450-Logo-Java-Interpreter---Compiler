import java.util.ArrayList;
import java.util.List;

import msu.cse.turtlegraphics.Turtle;

public class TurtleCall extends JavaFunction {
	
	// Don't use the stock template.
	

	public TurtleCall(String name, String signature, String argumentTypes) {
		// Return value of Turtle functions is always VOID
		super(name, signature, argumentTypes, Type.VOID, "turtle");
	}

	static final String msuCseTurtle = "msu/cse/turtlegraphics/Turtle/";
	
	static TurtleCall backward 		= new TurtleCall("backward", msuCseTurtle + "turtleBackward", "I");
	static TurtleCall beginfill 	= new TurtleCall("beginfill", msuCseTurtle + "turtleBeginFillPolygon", "");
	static TurtleCall circle 		= new TurtleCall("circle", msuCseTurtle + "turtleCircle", "II");
	static TurtleCall color 		= new TurtleCall("color", msuCseTurtle + "turtleSetColor", "III");
	static TurtleCall endfill 		= new TurtleCall("endfill", msuCseTurtle + "turtleEndFillPolygon", "");
	static TurtleCall forward 		= new TurtleCall("forward", msuCseTurtle + "turtleForward", "I");
	static TurtleCall left 			= new TurtleCall("left", msuCseTurtle + "turtleLeft", "I");
	static TurtleCall right 		= new TurtleCall("right", msuCseTurtle + "turtleRight", "I");
	static TurtleCall pendown 		= new TurtleCall("pendown", msuCseTurtle + "turtlePenDown", "");
	static TurtleCall penup 		= new TurtleCall("penup", msuCseTurtle + "turtlePenUp", "I");
	static TurtleCall setheading 	= new TurtleCall("setheading", msuCseTurtle + "turtleSetHeading", "D");
	
	/**
	 * Setpos is actually two function calls, which is a huge PITA for us.
	 * To take care of this, we can abuse the template to make the actual call,
	 * which will ignore the function signature. 
	 */
	static TurtleCall setpos 	= new TurtleCall("setpos", msuCseTurtle + "", "II");
	
	List<TurtleCall> turtleMethods = new ArrayList<TurtleCall>() {{
		add(backward);
		add(beginfill);
		add(circle);
		add(color);
		add(endfill);
		add(forward);
		add(left);
		add(right);
		add(pendown);
		add(penup);
		add(setheading);
		add(setpos);
	}};
	
	static TokenMap methods = new TokenMap<TurtleCall>() {
		{
			put(LogoTree.BACKWARD, backward);
			put(LogoTree.BACKWARD2, backward);
			put(LogoTree.BEGINFILL, beginfill);
			put(LogoTree.CIRCLE, circle);
			put(LogoTree.COLOR, color);
			put(LogoTree.ENDFILL, endfill);
			put(LogoTree.FORWARD, forward);
			put(LogoTree.FORWARD2, forward);
			put(LogoTree.LEFT, left);
			put(LogoTree.LEFT2, left);
			put(LogoTree.PENDOWN, pendown);
			put(LogoTree.PENUP, pendown);
			put(LogoTree.RIGHT, right);
			put(LogoTree.RIGHT2, right);
			put(LogoTree.SETHEADING, setheading);
			put(LogoTree.SETHEADING2, setheading);
			put(LogoTree.SETPOS, setpos);
		}
	};
}
