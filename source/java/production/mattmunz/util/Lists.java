package mattmunz.util;

import java.util.ArrayList;
import java.util.List;

// TODO Move to a more general module/package
public class Lists
{
  public static <T> List<T> concatenate(List<T> left, List<T> right)
  {
    ArrayList<T> items = new ArrayList<>(left);
    items.addAll(right);
    
    return items;
  }
}
