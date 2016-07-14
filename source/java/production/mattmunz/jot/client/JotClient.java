package mattmunz.jot.client;

import java.util.List;

import javax.ws.rs.core.GenericType;

import mattmunz.jot.Jot;
import mattmunz.service.ServiceClient;

// TODO Rework to use Sprint Boot/ReST?
public class JotClient extends ServiceClient
{
  public JotClient(String host, int port) { super(host, port, "Jot"); }

	public List<Jot> getJots() { return get("jots", new GenericType<List<Jot>>() {}); }
}
