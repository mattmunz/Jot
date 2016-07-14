package mattmunz.jot.logging;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The custom formatter is needed to get the timestamps correct (non-local).
 */
public class JotLogEventFormatter extends Formatter
{
  @Override
  public String format(LogRecord record) {

    String message = formatMessage(record);

    ZonedDateTime time 
      = ZonedDateTime.ofInstant(new Date(record.getMillis()).toInstant(), ZoneId.of("Z"));
    
    return new StringBuilder(50 + message.length())
                .append(time.format(ISO_DATE_TIME)).append(" ").append(message).append("\n")
                .toString();
  }
}
