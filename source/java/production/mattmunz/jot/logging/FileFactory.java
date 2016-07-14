package mattmunz.jot.logging;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFactory
{
  Path getPath(String fileName) { return Paths.get(fileName); }

  File createFile(String word) { return new File(word); }

  File createFile(File file, String word) { return new File(file, word); }
}
