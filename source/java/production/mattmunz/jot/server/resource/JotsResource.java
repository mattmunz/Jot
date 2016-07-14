package mattmunz.jot.server.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List; 

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import mattmunz.jot.Jot;
import mattmunz.jot.JotRepository;

@Path("jots")
public class JotsResource
{
  /** As needed, can inject this. */
  private final JotRepository repository = new JotRepository();
  
  @GET
  @Produces(APPLICATION_JSON)
  public List<Jot> getJots() { return repository.get(); }
  
  
  /**
   * @param identifier The time of the jot in millis. In the future this may be replaced 
   *        with a synthetic identifier. 
   */
  @GET
  @Path("/{identifier}")
  @Produces(APPLICATION_JSON)
  public Jot getJot(String identifier) 
  {
    String message 
      = "NYI. Lookup by Jot identifier [" + identifier + "] is not easy by the current "
        + "scheme since Jots are synthesized on demand.";
    throw new RuntimeException(message);
  }
  
  /**
   * TODO A post to this with an empty body ({}) results in NPE when it should result in 
   *      bad request params exception or something. Is caused by Jackson? 
   *      How to mark fields as required (non-null) in a way that Jackson understands?     
   */
  @POST
  @Produces(APPLICATION_JSON)
  public Response add(Jot jot) throws URISyntaxException 
  { 
    repository.add(jot);
    
    // TODO Refactor URL
    return Response.created(new URI("/Jot/jots/" + jot.getTime().toInstant().toEpochMilli())).build();
  }
}
