package mattmunz.jot;

import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

import mattmunz.jot.keyevent.KeyEvent;
import mattmunz.jot.keyevent.KeyEvents;
import mattmunz.util.Lists;

import org.junit.Test;

public class JotEventConverterTest
{
  private final JotEventConverter converter = new JotEventConverter();

  @Test
  public void merge()
  {
    ZonedDateTime leftTime = now();
    ZonedDateTime rightTime = now();
    
    assertEquals(new Jot(rightTime, "fo"), 
                 converter.merge(new Jot(leftTime, "f"), new Jot(rightTime, 'o')));
  }

  // TODO Move to ListsTest
  @Test
  public void concatenate()
  {
    assertEquals(asList("one", "two", "three"), 
                 Lists.concatenate(asList("one", "two"), asList("three")));
    assertEquals(emptyList(), Lists.concatenate(emptyList(), emptyList()));
    assertEquals(asList("one"), Lists.concatenate(asList("one"), emptyList()));
    assertEquals(asList("one"), Lists.concatenate(emptyList(), asList("one")));
  }

  // TODO Move to KeyEventsTest
  @Test
  public void compareByTime()
  {
    ZonedDateTime time1 = now();
    
    KeyEvent left = new KeyEvent(time1, 'f');
    KeyEvent right = new KeyEvent(time1.plusSeconds(2), 'b');
    
    assertEquals(-1, KeyEvents.compareByTime(left, right));
    assertEquals(0, KeyEvents.compareByTime(left, left));
    assertEquals(1, KeyEvents.compareByTime(right, left));
  }

  @Test
  public void addToMergedJots()
  {
    ZonedDateTime time1 = now();
    ZonedDateTime time2 = now();
    ZonedDateTime time3 = time2.plus(30, MILLIS);
    ZonedDateTime time4 = time2.plusSeconds(3);

    List<Jot> jots = asList(new Jot(time1, "f"), new Jot(time2, "a"));
    
    List<Jot> mergedJots = converter.addToMergedJots(jots, new Jot(time3, 'b'));
    assertEquals(2, mergedJots.size());
    assertEquals(new Jot(time3, "ab"), mergedJots.get(1));

    Jot newEvent = new Jot(time4, 'b');
    
    List<Jot> mergedJots2 = converter.addToMergedJots(jots, newEvent);
    assertEquals(3, mergedJots2.size());
    
    assertEquals(newEvent, mergedJots2.get(2));
  }

  @Test
  public void groupEvents()
  {
    ZonedDateTime time1 = now();
    ZonedDateTime time2 = time1.plusSeconds(3);
    ZonedDateTime time3 = time2.plus(30, MILLIS);

    KeyEvent event1 = new KeyEvent(time1, 'f');
    
    List<Jot> jots 
      = converter.groupEvents(asList(event1, new KeyEvent(time2, 'a'), new KeyEvent(time3, 'b')));
    
    assertEquals(2, jots.size());
    
    Iterator<Jot> iterator = jots.iterator();
    
    assertEquals(new Jot(event1), iterator.next());
    assertEquals(new Jot(time3, "ab"), iterator.next());
  }

  @Test
  public void groupEventsWithJots()
  {
    ZonedDateTime time1 = now();
    ZonedDateTime time2 = time1.plusSeconds(3);
    ZonedDateTime time3 = time2.plus(30, MILLIS);
    ZonedDateTime time4 = time3.plus(30, MILLIS);
    ZonedDateTime time5 = time4.plus(30, MILLIS);

    KeyEvent event1 = new KeyEvent(time1, 'f');
    Jot jot1 = new Jot(time4, "cde");
    
    List<Jot> jots 
      = converter.groupEvents(asList(event1, new KeyEvent(time2, 'a'), new KeyEvent(time3, 'b'), 
                                     new KeyEvent(time5, 'f')), asList(jot1));
    
    assertEquals(2, jots.size());
    
    Iterator<Jot> iterator = jots.iterator();
    
    assertEquals(new Jot(event1), iterator.next());
    assertEquals(new Jot(time5, "abcdef"), iterator.next());
  }
}
