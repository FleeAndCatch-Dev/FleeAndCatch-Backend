package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

public class Identification {

	private int id;
	private String address;
	private int port;
	private String type;
	private String subtype;
	
	public Identification(int pId, String pAddress, int pPort, String pType, String pSubtype){
		this.id = pId;
		this.address = pAddress;
		this.port = pPort;
		this.type = pType;
		this.subtype = pSubtype;
	}
	
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonIdentification = new JSONObject();
		jsonIdentification.put("id", id);
		jsonIdentification.put("address", address);
		jsonIdentification.put("port", port);
		jsonIdentification.put("type", type);
		jsonIdentification.put("subtype", subtype);
		
		return jsonIdentification;
	}

	public int getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}
}
