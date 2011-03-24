import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Formatter extends java.util.logging.Formatter {

private static final String MESSAGE_PATTERN = "[%tT %-7s] [%-7s] [%s]: %s %s\n";

  @Override
  public String format(LogRecord record) {
      return String.format(
        MESSAGE_PATTERN,
        new Date(record.getMillis()),
        record.getLevel(),
        Thread.currentThread().getName(),
        record.getLoggerName(),
        record.getMessage(),
        record.getThrown() == null ? "" : record.getThrown());
  }

  public static void main(String [] args){

    Logger l = Logger.getLogger(Formatter.class.getSimpleName());

    l.fine    ("Fine   Test");
    l.finer   ("Finer  Test");
    l.finest  ("Finest Test");
    l.warning ("Warning Test");
    l.severe  ("Severe Test");
    l.throwing("LoggerFormatter", "main", new Exception("Exception Test"));
  }
}
