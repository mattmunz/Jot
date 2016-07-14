package mattmunz.jot.keyevent;

import mattmunz.jot.logging.JotLogger;

import com.google.common.eventbus.Subscribe;

public class KeyEventLoggerFile
{
  private JotLogger jotLogger;
  
  @Subscribe
  public void log(KeyEvent event) 
  {
    // TODO Ignoring the Jot time here. Shouldn't do that...
    jotLogger.info("" + event.getCharacter()); 
  }

  /**
   * This method must always be called before log().
   */
  public void setJotLogger(JotLogger jotLogger) { this.jotLogger = jotLogger; }
}
