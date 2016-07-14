package mattmunz.jot.logging;

import static java.util.logging.Level.INFO; 
import static java.util.logging.Logger.getLogger;

import java.io.Closeable; 
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.Logger;

import mattmunz.logging.LogHelper;

/**
 * A logger with API similar to {@link java.util.logging.Logger} for logging Jots to disk.
 */
public class JotLogger implements Closeable
{
  private final LogHelper logHelper;
  private final Handler logRecordHandler;
  private final Logger logger;
  
  public JotLogger(Path logsPath, LogHelper logHelper) throws IOException 
  {
    this.logHelper = logHelper;
    logRecordHandler 
      = logHelper.createLogRecordHandler(logsPath, "jot", INFO, new JotLogEventFormatter());
    logger = getJotLogger(logRecordHandler);
  }
  
  @Override
  public void close() throws IOException
  {
    logger.removeHandler(logRecordHandler); 
    logRecordHandler.close();
  }

  public void info(String message) { logger.info(message); }
  
  private Logger getJotLogger(Handler logRecordHandler)
  {
    Logger jotLogger = getLogger(JotLogger.class.getName() + ".jot");
    
    logHelper.configure(jotLogger, INFO, logRecordHandler);
    
    return jotLogger;
  }
}
