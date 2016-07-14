package mattmunz.jot;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.ZonedDateTime;

public class TimeMarshaller
{
  public String getTimeText(ZonedDateTime time) { return time.format(ISO_DATE_TIME); }
  
  public ZonedDateTime parseDate(String dateText)
  {
    return ZonedDateTime.parse(dateText, ISO_DATE_TIME);
  }
}
