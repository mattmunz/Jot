package mattmunz.jot;

import static com.google.common.collect.Lists.newArrayList; 

import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class JotMarshaller
{
  static final String TIME_ATTRIBUTE_NAME = "time";
  private static final String TEXT_ATTRIBUTE_NAME = "text";
  
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();

  List<Jot> createJots(FindIterable<Document> documents)
  {
    return newArrayList(documents.map(this::createJot));
  }

  // TODO Can use Jackson for serialization instead????
  Document createDocument(Jot jot)
  {
    return new Document(TEXT_ATTRIBUTE_NAME, jot.getText())
                .append(TIME_ATTRIBUTE_NAME, getTimeText(jot));
  }
  
  // TODO Replace with Jackson?
  private Jot createJot(Document document) {
    
    String text = document.get(TEXT_ATTRIBUTE_NAME, String.class);
    String timeText = document.get(TIME_ATTRIBUTE_NAME, String.class);
    
    return new Jot(timeMarshaller.parseDate(timeText), text);
  }

  private String getTimeText(Jot jot) { return timeMarshaller.getTimeText(jot.getTime()); }
}
