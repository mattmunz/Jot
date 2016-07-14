package mattmunz.jot;

import static java.time.Duration.between; 
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Stream.concat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import mattmunz.jot.keyevent.KeyEvent;
import mattmunz.util.Lists;

class JotEventConverter
{
  /**
   * The maximum amount of time two events can be apart to be considered in the same group.
   */
  private static final Duration GROUPING_DURATION = Duration.ofSeconds(2);

  /**
   * TODO Now that we can have both Jots and KeyEvents in the DB, this method needs to support
   *      grouping a mixture of key events and jots. Maybe the easiest way to do that is 
   *      to convert all keyevents to jots first -- they'll be jots with a single character string only.
   *
   * TODO Rename to groupEventsAndJots?
   * 
   * @param events Does not need to be sorted.
   * @param jots Does not need to be sorted.
   * @return sorted and grouped Jots, where events/jots within 2s are put in the same Jot.
   */
  List<Jot> groupEvents(List<KeyEvent> events, List<Jot> jots)
  {
    // Two pass algorithm: First sort, then group jots by time.
    // TODO This implementation is inefficient (lost of array copying) and should be tightened up.
    
    return concat(jots.stream(), events.stream().map(Jot::new))
      .sorted(Jots::compareByTime).reduce(emptyList(), this::addToMergedJots, Lists::concatenate);
  }

  // TODO no longer needed?
  /**
   * @param events Does not need to be sorted.
   * @return sorted and grouped Jots, where events within 1s are put in the same Jot.
   */
  List<Jot> groupEvents(List<KeyEvent> events) { return groupEvents(events, emptyList()); }

  // TODO Move to Jot ctor or to Jots helper
  /**
   * @param left assume the lesser time value
   * @param right assume the greater time value
   * @return a new jot with concatenated text and latest time value
   */
  Jot merge(Jot left, Jot right)
  {
    return new Jot(right.getTime(), left.getText() + right.getText()); 
  }

  /**
   * Compare this event with the last jot in the list. If they are close, merge them, 
   * else add this event to the list.
   */      
  List<Jot> addToMergedJots(List<Jot> jots, Jot jot) 
  {
    if (jots.isEmpty()) { return asList(jot); }
    
    int lastIndex = jots.size() - 1;
    Jot lastJot = jots.get(lastIndex);
    
    if (between(lastJot.getTime(), jot.getTime()).compareTo(GROUPING_DURATION) <= 0) 
    {
      ArrayList<Jot> newJots = new ArrayList<Jot>(jots.subList(0, lastIndex));
      
      newJots.add(merge(lastJot, jot));
      
      return newJots;
    }
    
    ArrayList<Jot> newJots = new ArrayList<Jot>(jots);
    
    newJots.add(jot);
    
    return newJots;
  }
}
