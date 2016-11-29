package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

public class Connection extends Command {
	private Identification identification;
	
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
	public Connection(String pId, String pType, Identification pIdentification){
		super(pId, pType);
		this.identification = pIdentification;
	}
	
	/**
	 * <h1>Get command</h1>
	 * Get the command as a json string.
	 * 
	 * @return Json command as string.
	 * 
	 * @author ThunderSL94
	 */
	public String getCommand() throws JSONException{
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		
		return command.toString();
	}

	public Identification getIdentification() {
		return identification;
	}
}
