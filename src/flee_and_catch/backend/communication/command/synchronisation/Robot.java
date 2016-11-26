package flee_and_catch.backend.communication.command.synchronisation;

import org.json.JSONException;
import org.json.JSONObject;

public class Robot {
	private int id;
	private String type;
	
	public Robot(int pId, String pType){
		this.id = pId;
		this.type = pType;
	}
	
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
