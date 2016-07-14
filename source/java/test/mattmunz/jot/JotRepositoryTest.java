package mattmunz.jot;

import static java.util.Arrays.asList; 
import static java.time.ZonedDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.time.temporal.ChronoUnit.MILLIS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.jot.test.mongo.InMemoryFindIterable;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class JotRepositoryTest
{
  @Test
  public void getNoEvents()
  {
    List<Jot> jots = getJots(emptyList());
    
    assertTrue("Expected jots to be empty: " + jots, jots.isEmpty());
  }
  
  @Test
  public void getOneEvent()
  {
    Jot event1 = new Jot(now(), "f");

    List<Jot> jots = getJots(asList(event1));
    
    assertEquals(1, jots.size());
    
    assertEquals(event1, jots.iterator().next());
  }

  @Test
  public void getSparseEvents()
  {
    ZonedDateTime time1 = now();
    ZonedDateTime time2 = time1.plusDays(1);
    
    Jot event1 = new Jot(time1, "f");
    Jot event2 = new Jot(time2, "o");
    
    List<Jot> jots = getJots(asList(event1, event2));
    
    assertEquals(2, jots.size());
    assertTrue(jots.contains(event1));
    assertTrue(jots.contains(event2));
  }

  /**
   * This test assumes a grouping boundary of 2s.
   */
  @Test
  public void getGroupedEvents()
  {
    // Group 1
    ZonedDateTime time1 = now();
    ZonedDateTime time2 = time1.plus(100, MILLIS); 
    ZonedDateTime time3 = time2.plus(1999, MILLIS); 
    
    // Group 2
    ZonedDateTime time4 = time3.plus(2001, MILLIS);
    ZonedDateTime time5 = time4.plus(1234, MILLIS);
    ZonedDateTime time6 = time5.plus(1000, MILLIS);
    
    // Group3
    ZonedDateTime time7 = time6.plusSeconds(4);
    
    Jot event1 = new Jot(time1, "f");
    Jot event2 = new Jot(time2, "o");
    Jot event3 = new Jot(time3, "o");
    Jot event4 = new Jot(time4, "b");
    Jot event5 = new Jot(time5, "a");
    Jot event6 = new Jot(time6, "r");
    Jot event7 = new Jot(time7, "!");
    
    List<Jot> jots = getJots(asList(event1, event2, event3, event4, event5, event6, event7));
    
    assertEquals(3, jots.size());
    assertTrue(jots.contains(new Jot(time3, "foo")));
    assertTrue(jots.contains(new Jot(time6, "bar")));
    assertTrue(jots.contains(event7));
  }

  private List<Jot> getJots(List<Jot> events)
  {
    JotMarshaller marshaller = new JotMarshaller();

    FindIterable<Document> eventsIterable 
      = new InMemoryFindIterable(events.stream().map(marshaller::createDocument).collect(toList())); 
    
    @SuppressWarnings("unchecked")
    MongoCollection<Document> collection = mock(MongoCollection.class);
    when(collection.find()).thenReturn(eventsIterable);
    
    MongoDatabase database = mock(MongoDatabase.class);
    when(database.getCollection(any())).thenReturn(collection);
    
    MongoClient client = mock(MongoClient.class);
    when(client.getDatabase(any())).thenReturn(database);
    
    MongoClientFactory factory = mock(MongoClientFactory.class);
    when(factory.getClient()).thenReturn(client);
    
    JotRepository repository = new JotRepository(factory);
    
    return repository.get();
  }
}
