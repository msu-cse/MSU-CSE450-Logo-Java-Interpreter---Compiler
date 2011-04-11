import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Automatically initialize logging on startup.
 * 
 * @author zach
 */
public class LogoLogger {
	public static File loggingConfigFile = new File("logging.properties");

	static void configure() {
		LogManager lm = LogManager.getLogManager();

		if (!loggingConfigFile.exists() || !LogoProperties.ENABLE_LOGGING) {
			lm.getLogger("").setLevel(Level.OFF);
		} else {
			System.err.println("Configuring logging from " + loggingConfigFile.toString());
			// Load logging from the configuration
			try {
				FileInputStream fis = new FileInputStream(loggingConfigFile);
				lm.readConfiguration();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
