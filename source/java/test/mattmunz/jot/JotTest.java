package mattmunz.jot;

import static java.time.ZonedDateTime.now; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.time.ZoneId;

import mattmunz.service.ObjectMapperProvider;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JotTest
{
  // TODO Refactor together with other code that does this.
  private final ZoneId utc = ZoneId.of("UTC");
  
  @Test
  public void serialization() throws IOException
  {
    Jot jot1 = new Jot(now(utc), "foo");
    
    ObjectMapper mapper = new ObjectMapperProvider().getContext(Jot.class);
    
    String json = mapper.writer().writeValueAsString(jot1);
    
    System.out.print("json: " + json);
    
    // Test for a bug in an older version of Jackson.
    assertFalse(json.contains("["));
    
    Jot jot2 = mapper.readValue(json, Jot.class);
    
    assertEquals(jot1, jot2);
  }
}
