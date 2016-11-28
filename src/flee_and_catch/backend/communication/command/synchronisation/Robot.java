package flee_and_catch.backend.communication.command.synchronisation;

import org.json.JSONException;
import org.json.JSONObject;

public class Robot {
	private int id;
	private String type;
	
	/**
	 * <h1>Constructor</h1>
	 * Create new robot object for json command.
	 * 
	 * @param pId
	 * @param pType
	 * 
	 * @author ThunderSL94
	 */
	public Robot(int pId, String pType){
		this.id = pId;
		this.type = pType;
	}
	
	/**
	 * <h1>Get robot</h1>
	 * Get robot as json object.
	 * 
	 * @return
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public JSONObject GetRobot() throws JSONException{
		JSONObject jsonrobot = new JSONObject();
		jsonrobot.put("id", id);
		jsonrobot.put("type", type);
		
		return jsonrobot;
	}
	
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}
}
