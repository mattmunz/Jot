package mattmunz.jot.server.resource;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

// TODO Remove -- for testing only. Maybe replace with a general-purpose diagnostics endpoint. 
@Path("greeting")
public class GreetingResource
{
  @GET
  @Produces(TEXT_PLAIN)
  public String getHello() { return "Hello"; }
}
