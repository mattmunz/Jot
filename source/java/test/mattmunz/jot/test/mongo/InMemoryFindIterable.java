package mattmunz.jot.test.mongo;

import static java.util.Collections.emptyList;

import java.util.Collection; 
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;

/**
 * Iterates over an in-memory collection. Mostly for mocking, many methods not implemented.
 */
public class InMemoryFindIterable implements FindIterable<Document>
{
  private MongoCursor<Document> documents;

  public InMemoryFindIterable() { this(emptyList()); }
  
  public InMemoryFindIterable(List<Document> documents)
  {
    this.documents = new InMemoryMongoCursor<Document>(documents);
  }

  @Override
  public MongoCursor<Document> iterator() { return documents; }

  @Override
  public Document first() { throw new RuntimeException("nyi"); }

  @Override
  public <U> MongoIterable<U> map(Function<Document, U> mapper)
  {
    return new MappingIterable2<Document, U>(this, mapper);
  }

  @Override
  public void forEach(Block<? super Document> block) { throw new RuntimeException("nyi"); }

  @Override
  public <A extends Collection<? super Document>> A into(A target) 
  { 
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> filter(Bson filter) { throw new RuntimeException("nyi"); }

  @Override
  public FindIterable<Document> limit(int limit) { throw new RuntimeException("nyi"); }

  @Override
  public FindIterable<Document> skip(int skip) { throw new RuntimeException("nyi"); }

  @Override
  public FindIterable<Document> maxTime(long maxTime, TimeUnit timeUnit)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> modifiers(Bson modifiers)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> projection(Bson projection)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> sort(Bson sort)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> noCursorTimeout(boolean noCursorTimeout)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> oplogReplay(boolean oplogReplay)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> partial(boolean partial)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> cursorType(CursorType cursorType)
  {
    throw new RuntimeException("nyi");
  }

  @Override
  public FindIterable<Document> batchSize(int batchSize)
  {
    throw new RuntimeException("nyi");
  }
}
