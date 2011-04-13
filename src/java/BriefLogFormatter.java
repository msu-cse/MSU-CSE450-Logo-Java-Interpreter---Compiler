
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
 
public class BriefLogFormatter extends Formatter {
	
	private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
	private static final String lineSep = System.getProperty("line.separator");
	/**
	 * A Custom format implementation that is designed for brevity.
	 */
	public String format(LogRecord record) {
		String loggerName = record.getLoggerName();
		if(loggerName == null) {
			loggerName = "root";
		}
		StringBuilder output = new StringBuilder()
			.append(loggerName)
			.append("[")
			.append(record.getLevel()).append('|')
			.append(record.getSourceMethodName())
			.append("]: ")
			.append(record.getMessage()).append(' ')
			.append(lineSep);
		return output.toString();		
	}
 
}
 
