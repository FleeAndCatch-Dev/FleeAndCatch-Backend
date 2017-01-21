package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class ConnectionCommand extends Command {	
	private Device device;
	
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
	public ConnectionCommand(String pId, String pType, ClientIdentification pIdentification, Device pDevice){
		super(pId, pType, pIdentification);
		this.device = pDevice;
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
		command.put("device", device.getJSONObject());		
		
		return command.toString();
	}

	public Device getDevice() {
		return device;
	}	
}