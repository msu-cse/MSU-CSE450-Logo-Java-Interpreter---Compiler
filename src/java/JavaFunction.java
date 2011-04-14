import java.util.logging.Logger;


public class JavaFunction {
	public static final String defaultTemplate = "virtualCall";
	
	static Logger log = Logger.getLogger("JavaFunction");
	
	/** LOGO name of the function */
	String name;
	
	/** Java signature of the function */
	String signature;
	
	/** 
	 * List that contains the types of the arguments 
	 * 
	 * Note that with dynamic typing this isn't an issue to the user, but we
	 * have to make it work in Java.
	 */
	ArgumentTypeList argumentTypes;
	
	/**
	 * Return type
	 */
	Type returnType;

	/**
	 * Name of the template that will generate a call for this method.
	 * 
	 * Inputs to the template are:
	 * @param args The StringTemplates to evaluate the arguments
	 * @param signature The method signature
	 */
	String templateName = defaultTemplate;
		
	public JavaFunction(String name, String signature, String argumentTypes, Type returnValue) {
		this.name=name;
		this.signature=signature;
		this.argumentTypes = new ArgumentTypeList(argumentTypes);
		this.returnType = returnValue;		
	}
	
	public JavaFunction(String name, String signature, String argumentTypes, Type returnValue, String template) {
		this(name,signature,argumentTypes,returnValue);
		this.templateName=template;
	}
	
	public String toString() {
		// e.g. java/path/to/Class/method(IIIIIII)V
		return signature + "(" + argumentTypes + ")" + returnType.primitive;
	}
}
