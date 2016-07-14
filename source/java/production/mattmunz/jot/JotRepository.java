package mattmunz.jot;

import java.time.ZonedDateTime; 
import java.util.List;

import mattmunz.jot.keyevent.KeyEventRepository;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
public class JotRepository
{
  private final JotEventConverter eventConverter = new JotEventConverter();
  private final KeyEventRepository eventRepository;
  private final JotRepositoryMongoDB jotRepository;
  
  public JotRepository() { this(new MongoClientFactory()); }

  public JotRepository(MongoClientFactory clientFactory)
  {
    eventRepository = new KeyEventRepository(clientFactory);
    jotRepository = new JotRepositoryMongoDB(clientFactory);
  }

  public List<Jot> get() 
  { 
    return eventConverter.groupEvents(eventRepository.get(), jotRepository.get()); 
  }

  /**
   * TODO Now broken. Needs to interleave DB Jots with synthetic Jots from key events
   *      Maybe just remove this.
   */
  // TODO This isn't quite right with the grouping of Jots and the search mechanism.
  public List<Jot> getByTime(ZonedDateTime time)
  {
    return eventConverter.groupEvents(eventRepository.getByTime(time));
  }

  public void add(Jot jot) { jotRepository.add(jot); }
}
