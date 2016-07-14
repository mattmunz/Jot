package mattmunz.jot;

import static java.nio.file.Files.exists;  
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.google.common.jimfs.Jimfs.newFileSystem;
import static com.google.common.jimfs.Configuration.osX;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mattmunz.io.console.Console;
import mattmunz.jot.CLI;
import mattmunz.jot.logging.LogParser;

public class CLITest
{
  private FileSystem fileSystem;
  
  @Before
  public void setUp() { fileSystem = newFileSystem(osX()); }
  
  @After
  public void tearDown() throws IOException { fileSystem.close(); }
  
  @Test(expected = IllegalArgumentException.class)
  public void runWithInvalidArguments() throws IOException 
  {
    new CLI(fileSystem, mock(Console.class)).run(new String[]{ "foo", "bar"});
  }
  
  /**
   * Use case: Create the ./jot/logs directory if it doesn't exist
   */
  @Test
  public void runCreatesDefaultLogsDirectory() throws IOException 
  {
    Reader reader = mock(Reader.class);
    when(reader.read()).thenReturn(-1);
    
    Path logsPath = fileSystem.getPath("jot", "logs");
    
    System.out.println("logsPath: " + logsPath);
    
    assertFalse(exists(logsPath));
  	
    new CLI(fileSystem, createMockConsole(reader)).run(new String[]{});
    
    assertTrue(exists(logsPath));
  }
  
  /**
   * Use Case: Listen to the console stdin stream and on any keystroke, log that keystroke and the 
   *        time to a text file in that directory (by append)
   * @throws IOException 
   */
  @Test
  public void logSingleEvent() throws IOException, InterruptedException
  {
    assertSingleEventLogged(fileSystem.getPath("jot", "logs"), new String[]{});
  }
  
  /**
   * Use case: Can specify (optional) directory where logs are stored. Default: ./jot/logs
   */
  @Test
  public void usesProvidedLogsDirectory() throws IOException, InterruptedException 
  {
    assertSingleEventLogged(fileSystem.getPath("foo", "bar", "baz"), new String[]{ "foo/bar/baz" });
  }

  // TODO Too long
  private void assertSingleEventLogged(Path expectedLogsPath,
      String[] commandLineArguments) throws IOException, InterruptedException
  {
    Path logPath = expectedLogsPath.resolve("jot.0.0.log");
    
    assertFalse(exists(expectedLogsPath));
    assertFalse(exists(logPath));
    
    Reader reader = mock(Reader.class);
    when(reader.read()).thenReturn((int) 'f', -1);
    
    ZoneId utc = ZoneId.of("Z");
    
    ZonedDateTime startTime = ZonedDateTime.now(utc);
    
    // TODO For the timing test, instead use a latch and verify that the start/end interval is 
    //      smaller than some threshold
    //      Or maybe can just use greater or equal in comparison since rounding should be the same
    //      on read and write now.
    Thread.sleep(500); // To avoid rounding in timestamp representation 
    
    new CLI(fileSystem, createMockConsole(reader)).run(commandLineArguments);
    
    Thread.sleep(500); // To avoid rounding in timestamp representation 

    ZonedDateTime endTime = ZonedDateTime.now(utc);
    
    assertTrue(exists(expectedLogsPath));
    assertTrue(exists(logPath));
    
    List<Jot> entries = new LogParser().parse(logPath);
    
    assertEquals(1, entries.size());
    
    Jot entry = entries.iterator().next();
    
    assertEquals("f", entry.getText());
    
    ZonedDateTime entryTime = entry.getTime();
    
    assertBefore(startTime, entryTime);
    assertBefore(entryTime, endTime);
  }

  private void assertBefore(ZonedDateTime left, ZonedDateTime right)
  {
    assertTrue("Expected [" + left + "] to be before [" + right + "].", 
               left.compareTo(right) < 0);
  }

  private Console createMockConsole(Reader reader)
  {
    Console console = mock(Console.class);
    when(console.reader()).thenReturn(reader);
    
    return console;
  }
}
