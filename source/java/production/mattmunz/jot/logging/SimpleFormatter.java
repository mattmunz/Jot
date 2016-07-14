package mattmunz.jot.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A basic logger for formatting log records using a format string. Based on
 * {@link java.util.logging.SimpleFormatter}.
 */
public class SimpleFormatter extends Formatter
{
  /**
   * Format string for the log record.
   */
  private final String format;

  private final Date date = new Date();

  public SimpleFormatter(String format) { this.format = format; }

  @Override
  public synchronized String format(LogRecord record)
  {
    date.setTime(record.getMillis());
    
    return String.format(format, date, getSource(record), record.getLoggerName(), 
                         record.getLevel().getName(), formatMessage(record), 
                         getThrowableDescription(record));
  }

  private String getThrowableDescription(LogRecord record)
  {
    String throwable = "";
    if (record.getThrown() != null)
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.println();
      record.getThrown().printStackTrace(pw);
      pw.close();
      throwable = sw.toString();
    }
    return throwable;
  }

  private String getSource(LogRecord record)
  {
    if (record.getSourceClassName() == null) { return record.getLoggerName(); }
    
    String className = record.getSourceClassName();
    String methodName = record.getSourceMethodName();

    return methodName == null ? className : className + " " + methodName; 
  }
}
