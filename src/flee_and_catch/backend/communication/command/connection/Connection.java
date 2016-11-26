package flee_and_catch.backend.communication.command.connection;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.Command;

public class Connection extends Command {
	private Client client;
	
	public Connection(String pId, String pType, Client pClient){
		super(pId, pType);
		this.client = pClient;
	}
	
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
