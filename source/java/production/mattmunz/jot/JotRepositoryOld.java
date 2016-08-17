package mattmunz.jot;

import static com.mongodb.client.model.Filters.eq;   
import static mattmunz.jot.JotMarshaller.TIME_ATTRIBUTE_NAME;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.persistence.mongodb.MongoClientFactory;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
@Deprecated
public class JotRepositoryOld
{
  private static final String DB_NAME = "jot";
  private static final String COLLECTION_NAME = "jots";
  
  private final JotMarshaller jotMarshaller = new JotMarshaller();
  private final MongoClientFactory clientFactory; 
  
  public JotRepositoryOld() { this(new MongoClientFactory()); }

  public JotRepositoryOld(MongoClientFactory clientFactory)
  {
    this.clientFactory = clientFactory;
  }

  public void add(Jot jot)
  {
    try (MongoClient client = clientFactory.getClient())
    {
      getCollection(client).insertOne(jotMarshaller.createDocument(jot));
    }
  }

  public List<Jot> get()
  {
    try (MongoClient client = clientFactory.getClient())
    {
      return jotMarshaller.createJots(getCollection(client).find());
    }
  }

  /* TODO Remove. Not needed.
  // TODO This isn't quite right with the grouping of Jots and the search mechanism.
  public List<Jot> getByTime(ZonedDateTime time)
  {
    try (MongoClient client = new MongoClient())
    {
      Bson filter = eq(TIME_ATTRIBUTE_NAME, jotMarshaller.getTimeText(time));
      
      return jotMarshaller.createJots(getCollection(client).find(filter));
    }
  }
  */
  
  private MongoCollection<Document> getCollection(MongoClient client) 
  {
    return client.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);
  }

  public void removeAll()
  {
    try (MongoClient client = new MongoClient()) { getCollection(client).drop(); }
  }
}
