package mattmunz.jot;

import static java.lang.System.console;  
  
import static java.util.Collections.emptyList;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.nio.file.Files.createDirectories;
import static java.util.logging.Logger.getLogger;
import static java.time.ZonedDateTime.now;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Parameter;
import mattmunz.cli.commandline.Parser;
import mattmunz.io.console.Console;
import mattmunz.jot.keyevent.KeyEvent;
import mattmunz.jot.keyevent.KeyEventLoggerDB;
import mattmunz.jot.keyevent.KeyEventLoggerFile;
import mattmunz.jot.logging.JotLogger;
import mattmunz.logging.FileHandlerProvider;
import mattmunz.logging.LogHelper;

public class CLI
{
  private static final String WELCOME_MESSAGE 
    = "Welcome to Jot. Type anything to record a moment you'd like to remember!";

  private static final Logger logger = getLogger(CLI.class.getName()); 
      
  private final FileSystem fileSystem;
  private final Console console;
  private final Path defaultLogsPath;
  private final KeyEventLoggerFile jotEventLoggerFile = new KeyEventLoggerFile();

  private final EventBus eventBus;

  public static void main(String[] arguments) throws IOException { new CLI().run(arguments); }
  
	/**
	 * Visible for testing only.
	 */
	CLI(FileSystem fileSystem, Console console) 
	{
		if (fileSystem == null) { throw new IllegalArgumentException("null fileSystem"); }
		
	  this.fileSystem = fileSystem; 
	  this.console = console;
	  
	  defaultLogsPath = fileSystem.getPath("jot", "logs");
	  
	  eventBus = new EventBus(CLI.class.getPackage().getName());
	  eventBus.register(jotEventLoggerFile);
    eventBus.register(new KeyEventLoggerDB());
	}
	
	private CLI() { this(FileSystems.getDefault(), new Console(console())); } 

	/**
	 * From dev task description:
	 * 
	 * Start with a commandline application that on key press records that key, and time date to 
	 * a given file. Can be left open in a terminal window all the time. Just take input from stdin.
	 * 
	 * Command line arguments:
	 * 
	 * (optional) directory -- The directory where logs are stored. Default: ./jot/logs
	 * 
	 * Example commands:
	 * 
	 * jot
	 * 
	 * jot ./Logs/February
	 * 
	 * @throws IllegalArgumentException if the wrong number of arguments are provided
	 * 
	 * Visible for testing only.
	 */
  void run(String[] arguments) throws IllegalArgumentException, IOException
	{
    logger.fine("Classpath: " + System.getProperty("java.class.path"));
  	
		CommandLine commandLine = new Parser(emptyList(), 0, 1).parse(arguments);
    
    List<Parameter<?>> parameters = commandLine.getParameters();
    
    logger.fine("Parameters: " + parameters);
    logger.fine("Options: " + commandLine.getOptions());

    Path logsPath = getLogsDirectoryPath(commandLine);
    
    createDirectories(logsPath);
    
    LogHelper logHelper = new LogHelper(new FileHandlerProvider());
    logHelper.configureRootWithHandler(logsPath, "application", INFO);

    try (JotLogger jotLogger = new JotLogger(logsPath, logHelper)) 
    {
      jotEventLoggerFile.setJotLogger(jotLogger);
      
      console.format(WELCOME_MESSAGE + "\n\n");

      postJotEvents(jotLogger);
    
      console.format("\nGoodbye.\n");
    } 
	}

  private Path getLogsDirectoryPath(CommandLine commandLine)
  {
    List<Parameter<?>> parameters = commandLine.getParameters();
    
    if (parameters.isEmpty()) 
    {
      logger.fine("No command parameters so using the default logs directory: " + defaultLogsPath);
      
      return defaultLogsPath;
    }
    
    // TODO Would be better to use that new getParameters method here
    String logsDirectoryPathText = parameters.get(0).toString();

    try { return fileSystem.getPath(logsDirectoryPathText); }
    catch (InvalidPathException e) 
    {
      String message = "Invalid logs directory path: " + logsDirectoryPathText 
                       + ". Using default instead: " + defaultLogsPath;
      logger.log(SEVERE, message, e);
      
      return defaultLogsPath;
    }
  }

  /**
   * TODO By this method, nothing gets logged until someone hits enter. This requires changing a tty
   *      setting (using stty) in the calling script and changing it back after the program runs, 
   *      which isn't ideal. Preferably these setting changes could be made in the Java code, 
   *      presumably by making exec calls to stty.  
   */
  private void postJotEvents(JotLogger jotLogger) throws IOException
  {
    Reader reader = console.reader();
  
    int character;
  
    while ((character = reader.read()) != -1) 
    { 
      eventBus.post(new KeyEvent(now(), (char) character));
    }
  }
}
