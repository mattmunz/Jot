package mattmunz.jot.logging;

import static java.nio.file.Files.readAllLines;  

import java.io.IOException;  
import java.nio.file.Path;
import java.util.List;

import mattmunz.jot.Jot;
import mattmunz.persistence.mongodb.TimeMarshaller;
import static java.util.stream.Collectors.toList;

public class LogParser
{
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();

  /**
   * This method may be wasteful for large files. A streaming approach may make more sense 
   * for large files. 
   */
  public List<Jot> parse(Path logPath) throws IOException
  {
    return readAllLines(logPath).stream().map(this::parseLine).collect(toList());
  }
  
  private Jot parseLine(String line)
  {
    String[] tokens = line.split(" ");
    
    int tokenCount = tokens.length;
    
    if (tokenCount != 2) 
    { 
      throw new IllegalStateException("Expected 2 tokens but there were " + tokenCount 
                                      + ". Line: " + line); 
    }
    
    return new Jot(timeMarshaller.parseDate(tokens[0].trim()), tokens[1]);
  }
}
