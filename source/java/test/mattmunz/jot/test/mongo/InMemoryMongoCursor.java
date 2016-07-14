package mattmunz.jot.test.mongo;

import java.util.Iterator; 
import java.util.List;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;

public class InMemoryMongoCursor<T> implements MongoCursor<T>
{
  private final Iterator<T> items;

  InMemoryMongoCursor(List<T> documents) { this.items = documents.iterator(); }

  @Override
  public void close() {}

  @Override
  public boolean hasNext() { return items.hasNext(); }

  @Override
  public T next() { return items.next(); }

  @Override
  public T tryNext() { throw new RuntimeException("nyi"); }

  @Override
  public ServerCursor getServerCursor() { throw new RuntimeException("nyi"); }

  @Override
  public ServerAddress getServerAddress() { throw new RuntimeException("nyi"); }
}
