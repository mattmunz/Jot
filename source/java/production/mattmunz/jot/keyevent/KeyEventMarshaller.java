package mattmunz.jot.keyevent;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import mattmunz.jot.TimeMarshaller;

import org.bson.Document;

import com.mongodb.client.FindIterable;

// TODO Refactor together with JotMarshaller
public class KeyEventMarshaller
{
  static final String TIME_ATTRIBUTE_NAME = "time";
  private static final String TEXT_ATTRIBUTE_NAME = "text";
  
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();

  List<KeyEvent> createKeyEvents(FindIterable<Document> documents)
  {
    return newArrayList(documents.map(this::createEvent));
  }

  // TODO Can use Jackson for serialization instead????
  Document createDocument(KeyEvent event)
  {
    return new Document(TIME_ATTRIBUTE_NAME, getTimeText(event))
                .append(TEXT_ATTRIBUTE_NAME, event.getCharacter());
  }

  private String getTimeText(KeyEvent event) { return timeMarshaller.getTimeText(event.getTime()); }
  
  // TODO Replace with Jackson?
  private KeyEvent createEvent(Document document) {
    
    String text = document.get(TEXT_ATTRIBUTE_NAME, String.class);
    String timeText = document.get(TIME_ATTRIBUTE_NAME, String.class);
    
    return new KeyEvent(timeMarshaller.parseDate(timeText), text);
  }
}
