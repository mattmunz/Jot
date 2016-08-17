package mattmunz.jot.keyevent;

import static com.mongodb.client.model.Filters.eq;  
import static mattmunz.jot.keyevent.KeyEventMarshaller.TIME_ATTRIBUTE_NAME;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.persistence.mongodb.MongoClientFactory;
import mattmunz.persistence.mongodb.MongoRepository;
import mattmunz.persistence.mongodb.TimeMarshaller;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
public class KeyEventRepository extends MongoRepository
{
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();
  private final KeyEventMarshaller eventMarshaller = new KeyEventMarshaller();
  
  public KeyEventRepository() { this(new MongoClientFactory()); }
  
  public KeyEventRepository(MongoClientFactory clientFactory)
  {
    super(clientFactory, "jot", "keyEvents");
  }

  public void add(KeyEvent event) { consumeWithCollection(this::add, event); }

  public List<KeyEvent> get() { return getWithCollection(this::get); }

  // TODO This isn't quite right with the grouping of Jots and the search mechanism.
  public List<KeyEvent> getByTime(ZonedDateTime time)
  {
    return applyWithCollection(this::getByTime, time);
  }

  private void add(MongoCollection<Document> collection, KeyEvent event)
  {
    collection.insertOne(eventMarshaller.createDocument(event));
  }

  private List<KeyEvent> get(MongoCollection<Document> collection)
  {
    return eventMarshaller.createKeyEvents(collection.find());
  }

  private List<KeyEvent> getByTime(MongoCollection<Document> collection, ZonedDateTime time)
  {
    Bson filter = eq(TIME_ATTRIBUTE_NAME, timeMarshaller.getTimeText(time));
      
    return eventMarshaller.createKeyEvents(collection.find(filter));
  }
}
