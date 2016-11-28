package flee_and_catch.backend.communication.command.connection;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.Command;

public class Connection extends Command {
	private Client client;
	
	/**
	 * <h1>Constructor</h1>
	 * Create new connection object for json command.
	 * 
	 * @param pId
	 * @param pType
	 * @param pClient
	 * 
	 * @author ThunderSL94
	 */
	public Connection(String pId, String pType, Client pClient){
		super(pId, pType);
		this.client = pClient;
	}
	
	/**
	 * <h1>Get command</h1>
	 * Get the command as a json string.
	 * 
	 * @return Json command as string.
	 * 
	 * @author ThunderSL94
	 */
	public String GetCommand() throws JSONException{
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("client", client.GetClient());
		
		return command.toString();
	}

	public Client getClient() {
		return client;
	}
}
