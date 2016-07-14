package mattmunz.jot;

import static java.time.ZonedDateTime.now;

import static org.junit.Assert.assertFalse; 

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;

/**
 * A functional test that accesses the db directly.
 */
public class JotRepositoryFunctionalTest
{
  @Test
  public void add()
  {
    ZonedDateTime time = now();

    JotRepository repository = new JotRepository();
    
    // TODO This won't work because the add needs to be done on key event repository, See how to share it with JotRepository...
    // repository.add(new Jot(time, "x"));
    
    List<Jot> jots = repository.getByTime(time);
    
    assertFalse(jots.isEmpty());
    
    jots.forEach(System.out::println);
  }
  
  @Test
  public void get()
  {
    JotRepository repository = new JotRepository();
    
    List<Jot> jots = repository.get();
    
    assertFalse(jots.isEmpty());
    
    jots.forEach(System.out::println);
  }
}
