package mattmunz.jot;

import static org.junit.Assert.assertFalse;

import java.util.List;

import mattmunz.jot.client.JotClient;

import org.junit.Test;

// TODO Add an annotation like @FunctionalTest
/**
 * A functional test of the GetJots endpoint.
 */
public class GetJotsTest
{
  // TODO Add a setup method where we do a basic sanity to confirm that the remote host is 
  //      online and ready for testing.
  @Test
  public void getJots()
  {
  	  JotClient client = new JotClient("localhost", 8080);
    
    List<Jot> jots = client.getJots();
    
    assertFalse(jots.isEmpty());
    
    jots.forEach(System.out::println);
  }
}
