package mattmunz.jot;

import java.util.List;

import mattmunz.persistence.mongodb.MongoClientFactory;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
public class JotRepositoryMongoDB
{
  private static final String DB_NAME = "jot";
  private static final String COLLECTION_NAME = "jots";
  
  private final JotMarshaller jotMarshaller = new JotMarshaller();
  private final MongoClientFactory clientFactory; 
  
  public JotRepositoryMongoDB() { this(new MongoClientFactory()); }

  public JotRepositoryMongoDB(MongoClientFactory clientFactory)
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

  public void removeAll()
  {
    try (MongoClient client = new MongoClient()) { getCollection(client).drop(); }
  }
  
  private MongoCollection<Document> getCollection(MongoClient client) 
  {
    return client.getDatabase(DB_NAME).getCollection(COLLECTION_NAME);
  }
}
