package mattmunz.jot.keyevent;

public class KeyEvents
{
  public static int compareByTime(KeyEvent left, KeyEvent right)
  {
    return left.getTime().compareTo(right.getTime());
  }
}
