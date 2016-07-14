package mattmunz.jot;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.jot.keyevent.KeyEvent;
import mattmunz.property.PropertiedObject;
import mattmunz.property.Property;
import mattmunz.property.PropertyListBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Jot implements PropertiedObject
{
  private final ZonedDateTime time;
  private final String text;
  
  public Jot(@JsonProperty("time") ZonedDateTime time, @JsonProperty("text") String text)
  {
    this.time = time;
    this.text = text;
  }

  public Jot(ZonedDateTime time, char character) { this(time, "" + character); }

  public Jot(KeyEvent event) { this(event.getTime(), event.getCharacter()); }

  public String getText() { return text; }

  public ZonedDateTime getTime() { return time; }

  @Override
  public String toString() { return getToStringText(); }

  @Override
  public int hashCode() { return getHashCode(); }

  @Override
  public boolean equals(Object other) { return isEqualTo(other); }
  
  @JsonIgnore
  @Override
  public List<Property> getProperties()
  {
    return new PropertyListBuilder().add("time", time).add("text", text).build();
  }
}
