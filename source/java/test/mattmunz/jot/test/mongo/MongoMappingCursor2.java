package mattmunz.jot.test.mongo;

/*
 * Derived from a mongo class that was non-public.
 */

import com.mongodb.Function;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;

import static com.mongodb.assertions.Assertions.notNull;

class MongoMappingCursor2<T, U> implements MongoCursor<U> {
    private final MongoCursor<T> proxied;
    private final Function<T, U> mapper;

    public MongoMappingCursor2(final MongoCursor<T> proxied, final Function<T, U> mapper) {
        this.proxied = notNull("proxied", proxied);
        this.mapper = notNull("mapper", mapper);
    }

    @Override
    public void close() {
        proxied.close();
    }

    @Override
    public boolean hasNext() {
        return proxied.hasNext();
    }

    @Override
    public U next() {
        return mapper.apply(proxied.next());
    }

    @Override
    public U tryNext() {
        T proxiedNext = proxied.tryNext();
        if (proxiedNext == null) {
            return null;
        } else {
            return mapper.apply(proxiedNext);
        }
    }

    @Override
    public void remove() {
        proxied.remove();
    }

    @Override
    public ServerCursor getServerCursor() {
        return proxied.getServerCursor();
    }

    @Override
    public ServerAddress getServerAddress() {
        return proxied.getServerAddress();
    }
}
