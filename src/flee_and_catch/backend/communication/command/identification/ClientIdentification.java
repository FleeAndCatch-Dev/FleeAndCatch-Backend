package flee_and_catch.backend.communication.command.identification;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.component.IdentificationType;

public class ClientIdentification extends Identification {

	private String type;
	private String address;
	private int port;
	
	public ClientIdentification(int pId, String pAddress, int pPort, String pType){
		this.id = pId;
		this.address = pAddress;
		this.port = pPort;
		this.type = IdentificationType.valueOf(pType).toString();
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
		jsonIdentification.put("type", type);
		jsonIdentification.put("address", address);
		jsonIdentification.put("port", port);
		
		return jsonIdentification;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
}