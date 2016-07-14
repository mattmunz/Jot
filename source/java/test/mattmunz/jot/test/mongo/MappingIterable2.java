package mattmunz.jot.test.mongo;

/*
 * Taken from a mongo class that wasn't public.
 */

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;

import java.util.Collection;

public class MappingIterable2<U, V> implements MongoIterable<V>
{
  private final MongoIterable<U> iterable;
  private final Function<U, V> mapper;

  public MappingIterable2(final MongoIterable<U> iterable, final Function<U, V> mapper) {
      this.iterable = iterable;
      this.mapper = mapper;
  }

  @Override
  public MongoCursor<V> iterator() {
      return new MongoMappingCursor2<U, V>(iterable.iterator(), mapper);
  }

  @Override
  public V first() {
      MongoCursor<V> iterator = iterator();
      if (!iterator.hasNext()) {
          return null;
      }
      return iterator.next();
  }

  @Override
  public void forEach(final Block<? super V> block) {
      iterable.forEach(new Block<U>() {
          @Override
          public void apply(final U document) {
              block.apply(mapper.apply(document));
          }
      });
  }

  @Override
  public <A extends Collection<? super V>> A into(final A target) {
      forEach(new Block<V>() {
          @Override
          public void apply(final V v) {
              target.add(v);
          }
      });
      return target;
  }

  @Override
  public MappingIterable2<U, V> batchSize(final int batchSize) {
      iterable.batchSize(batchSize);
      return this;
  }

  @Override
  public <W> MongoIterable<W> map(final Function<V, W> newMap) {
      return new MappingIterable2<V, W>(this, newMap);
  }
}
