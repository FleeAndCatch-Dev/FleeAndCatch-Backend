package flee_and_catch.backend.communication.command.connection;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {
	private int id;
	private String type;
	private String subtype;
	
	public Client(int pId, String pType, String pSubType){
		this.id = pId;
		this.type = pType;
		this.subtype = pSubType;
	}
	
	public JSONObject GetClient() throws JSONException{
		JSONObject jsonclient = new JSONObject();
		jsonclient.put("id", id);
		jsonclient.put("type", type);
		jsonclient.put("subtype", subtype);
		
		return jsonclient;
	}
	
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}
}
