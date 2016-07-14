package mattmunz.jot.keyevent;

import static com.mongodb.client.model.Filters.eq; 
import static mattmunz.jot.keyevent.KeyEventMarshaller.TIME_ATTRIBUTE_NAME;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.jot.MongoClientFactory;
import mattmunz.jot.TimeMarshaller;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
public class KeyEventRepository
{
  private static final String DB_NAME = "jot";
  private static final String COLLECTION_NAME = "keyEvents";
  
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();
  private final KeyEventMarshaller eventMarshaller = new KeyEventMarshaller();
  private final MongoClientFactory clientFactory; 
  
  public KeyEventRepository() { this(new MongoClientFactory()); }

  public KeyEventRepository(MongoClientFactory clientFactory)
  {
    this.clientFactory = clientFactory;
  }

  public void add(KeyEvent event)
  {
    try (MongoClient client = clientFactory.getClient())
    {
      getCollection(client).insertOne(eventMarshaller.createDocument(event));
    }
  }

  public List<KeyEvent> get()
  {
    try (MongoClient client = clientFactory.getClient())
    {
      return eventMarshaller.createKeyEvents(getCollection(client).find());
    }
  }

  // TODO This isn't quite right with the grouping of Jots and the search mechanism.
  public List<KeyEvent> getByTime(ZonedDateTime time)
  {
    try (MongoClient client = new MongoClient())
    {
      Bson filter = eq(TIME_ATTRIBUTE_NAME, timeMarshaller.getTimeText(time));
      
      return eventMarshaller.createKeyEvents(getCollection(client).find(filter));
    }
  }

  // TODO Move to common superclass or utility method?
  private MongoCollection<Document> getCollection(MongoClient client) 
  {
    return client.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);
  }
}
