package mattmunz.jot.keyevent;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.property.PropertiedObject;
import mattmunz.property.Property;
import mattmunz.property.PropertyListBuilder;

public class KeyEvent implements PropertiedObject
{
  private static char getCharacter(String text)
  {
    if (text.length() != 1) 
    { 
      String message = "Only single-character strings allowed. Actual: " + text;
      throw new IllegalArgumentException(message); 
    }
    
    return text.charAt(0);
  }

  private final ZonedDateTime time;
  private final char character;

  public KeyEvent(ZonedDateTime time, String text) { this(time, getCharacter(text)); }

  public KeyEvent(ZonedDateTime time, char character)
  {
    this.time = time;
    this.character = character;
  }

  public ZonedDateTime getTime() { return time; }

  public char getCharacter() { return character; }

  @Override
  public String toString() { return getToStringText(); }

  @Override
  public int hashCode() { return getHashCode(); }

  @Override
  public boolean equals(Object other) { return isEqualTo(other); }
  
  @Override
  public List<Property> getProperties()
  {
    return new PropertyListBuilder().add("time", time).add("character", character).build();
  }
}
