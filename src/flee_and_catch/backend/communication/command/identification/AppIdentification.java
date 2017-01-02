package flee_and_catch.backend.communication.command.identification;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.component.RoleType;

public class AppIdentification extends Identification {

	private String roletype;
	
	public AppIdentification(int pId, String pRoleType) {
		this.id = pId;
		this.type = IdentificationType.valueOf(IdentificationType.App.toString()).toString();
		this.roletype = RoleType.valueOf(pRoleType).toString();
	}
	
	public AppIdentification(int pId, String pType, String pRoleType) {
		this.id = pId;
		this.type = IdentificationType.valueOf(pType).toString();
		this.roletype = RoleType.valueOf(pRoleType).toString();
	}
	
	@Override
	public JSONObject getJSONObject() throws JSONException {
		JSONObject jsonIdentification = new JSONObject();
		jsonIdentification.put("id", id);
		jsonIdentification.put("type", type);
		jsonIdentification.put("roletype", roletype);
		
		return jsonIdentification;
	}

	public String getRoletype() {
		return roletype;
	}
}
