package mattmunz.jot;

// TODO Refactor together with JeyEvents?
public class Jots
{
  public static int compareByTime(Jot left, Jot right)
  {
    return left.getTime().compareTo(right.getTime());
  }
}
