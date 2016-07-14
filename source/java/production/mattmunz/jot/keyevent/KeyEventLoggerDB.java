package mattmunz.jot.keyevent;

import com.google.common.eventbus.Subscribe;

public class KeyEventLoggerDB
{
  private final KeyEventRepository eventRepository = new KeyEventRepository();
  
  @Subscribe
  public void log(KeyEvent event) { eventRepository.add(event); }
}
