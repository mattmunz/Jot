package mattmunz.jot.migration;

import java.util.List; 

import mattmunz.jot.Jot;
import mattmunz.jot.JotRepositoryOld;
import mattmunz.jot.keyevent.KeyEvent;
import mattmunz.jot.keyevent.KeyEventRepository;

class JotToKeyEventMigrator
{
  public static void main(String[] args)
  {
    new JotToKeyEventMigrator().migrateAllJotsToKeyEvents();
    
    System.out.println("Success!");
  }
  
  private final JotRepositoryOld jotRepository = new JotRepositoryOld();
  private final KeyEventRepository keyEventRepository = new KeyEventRepository();
  
  // Don't do this any more!
  void migrateAllJotsToKeyEvents() 
  {
    List<Jot> jots = jotRepository.get();
    
    for (Jot jot : jots) { keyEventRepository.add(new KeyEvent(jot.getTime(), jot.getText())); }
    
    verifyCopySuccessful(jots);
    
    // TODO What to do here -- make it interactive?
    // jotRepository.removeAll();
  }

  private void verifyCopySuccessful(List<Jot> jots)
  {
    List<KeyEvent> events = keyEventRepository.get();
    
    int jotsCount = jots.size();
    int eventsCount = events.size();
    
    if (jotsCount != eventsCount)
    {
      throw new IllegalStateException("expected " + jotsCount + " events but there were " + eventsCount);
    }
  }
}
