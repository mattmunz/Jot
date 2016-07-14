package mattmunz.jot;

import static java.util.Arrays.asList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * A functional test of basic Mongo DB functionality, from the MongoDB tutorial.
 */
public class MongoDBTest
{
  DateFormat format 
    = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

  @Test
  public void connectToTestDB() throws ParseException
  {
    try (MongoClient client = new MongoClient())
    {
      MongoDatabase db = getTestDB(client);

      addRestaurant(db);

      MongoCollection<Document> restaurants = db.getCollection("restaurants");
      
      System.out.println("Restaurant count: " + restaurants.count());
      
      FindIterable<Document> iterable = restaurants.find();
      
      iterable.forEach((Block<Document>) System.out::println);
    }
  }

  private void addRestaurant(MongoDatabase db) throws ParseException
  {
    db.getCollection("restaurants").insertOne(
        new Document("address", new Document().append("street", "2 Avenue")
            .append("zipcode", "10075").append("building", "1480")
            .append("coord", asList(-73.9557413, 40.7720266)))
            .append("borough", "Manhattan")
            .append("cuisine", "Italian")
            .append(
                "grades",
                asList(
                    new Document()
                        .append("date", format.parse("2014-10-01T00:00:00Z"))
                        .append("grade", "A").append("score", 11),
                    new Document()
                        .append("date", format.parse("2014-01-16T00:00:00Z"))
                        .append("grade", "B").append("score", 17)))
            .append("name", "Vella").append("restaurant_id", "41704620"));
  }

  private MongoDatabase getTestDB(MongoClient client) { return client.getDatabase("test"); }
}
