package mattmunz.jot.server;

import static org.glassfish.jersey.server.ServerProperties.TRACING;

import javax.ws.rs.ApplicationPath;

import mattmunz.service.ObjectMapperProvider;

import org.glassfish.jersey.server.ResourceConfig;

/** This path name results in a root URL like Jot/. */ 
@ApplicationPath("")
public class Application extends ResourceConfig
{
  public Application() 
  {
    // TODO Is this the right package for classpath scanning?
    // TODO Use reflection to get the package object here.
    packages("mattmunz.jot.server.resource"); 

    property(TRACING, "ALL");
    
    register(ObjectMapperProvider.class);
  }
}
