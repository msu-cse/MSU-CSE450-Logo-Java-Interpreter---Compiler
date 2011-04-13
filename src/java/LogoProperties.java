import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class LogoProperties extends Properties {

	
	static String propertiesFile = "logo.properties";
	static Logger log = Logger.getLogger("LogoProperties");

	static String interpret = "interpret";
	static String stringTemplateFile = "stringTemplateFile";
	static String generateBytecode = "generateBytecode";
	static String generateDotfile = "generateDotfile";
	static String dotfile = "dotfile";
	static String enableLogging = "enableLogging";
	
	/**
	 * Instance backing the static methods.
	 */
	static LogoProperties instance = new LogoProperties() {
		{
			defaults = defaultValues;
		}
	};

	/**
	 * Properties to be set in the initialization code.
	 */
	public static String STRING_TEMPLATES_FILE;
	public static Boolean GENERATE_BYTECODE;
	public static Boolean INTERPRET_INPUT;
	public static Boolean GENERATE_DOTFILE;
	public static String DOTFILE;
	public static Boolean ENABLE_LOGGING;

	/**
	 * Default values
	 */
	static Properties defaultValues = new Properties() {
		private static final long serialVersionUID = 1L;

		{
			setProperty(stringTemplateFile, "LogoST.stg");
			setProperty(interpret, "false");
			setProperty(generateBytecode, "true");
			setProperty(generateDotfile, "false");
			setProperty(dotfile,"output.dot");
			setProperty(enableLogging,"false");
		}
	};
	
	

	/**
	 * Initialization code
	 */
	static {
		File propsFile = new File(propertiesFile);

		try {
			FileInputStream fis = new FileInputStream(propsFile);
			instance.load(fis);
			fis.close();
		} catch (IOException e) {
			log.warning("Could not load properties file " + propertiesFile
					+ ":\n" + e.toString());
		}
		
		// Override with environment properties
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
        	instance.setProperty(envName, env.get(envName));
        }
		
		STRING_TEMPLATES_FILE = instance.getProperty(stringTemplateFile);
		INTERPRET_INPUT = Boolean.valueOf(instance.getProperty(interpret));
		GENERATE_BYTECODE = Boolean.valueOf(instance.getProperty(generateBytecode));
		GENERATE_DOTFILE = Boolean.valueOf(instance.getProperty(generateDotfile));
		DOTFILE = instance.getProperty(dotfile);
		ENABLE_LOGGING = Boolean.valueOf(instance.getProperty(enableLogging));
	}

}
