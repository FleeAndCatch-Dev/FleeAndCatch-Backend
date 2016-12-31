package flee_and_catch.backend.communication.command.identification;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotIdentification {
	private String subtype;
	private String roletype;
	
	public RobotIdentification(String pSubtype, String pRoletype){
		this.subtype = pSubtype;
		this.roletype = pRoletype;
	}
	
	/**
	 * <h1>Get identification</h1>
	 * Get identification as json object.
	 * 
	 * @return Identification as json object.
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonIdentification = new JSONObject();
		jsonIdentification.put("subtype", subtype);
		jsonIdentification.put("role", roletype);
		
		return jsonIdentification;
	}

	public String getSubtype() {
		return this.subtype;
	}

	public String getRole() {
		return this.roletype;
	}
}
