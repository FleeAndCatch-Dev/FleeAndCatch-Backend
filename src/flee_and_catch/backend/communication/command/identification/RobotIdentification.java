package flee_and_catch.backend.communication.command.identification;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.component.RobotType;
import flee_and_catch.backend.communication.command.component.RoleType;

public class RobotIdentification extends Identification {
	private String subtype;
	private String roletype;
	
	public RobotIdentification(int pId, String pSubtype, String pRoletype){
		this.id = pId;
		this.subtype = RobotType.valueOf(pSubtype).toString();
		this.roletype = RoleType.valueOf(pRoletype).toString();
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
		jsonIdentification.put("id", id);
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
