package flee_and_catch.backend.device.app;


import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.identification.ClientIdentification;
import flee_and_catch.backend.device.Device;

public class App implements Device {
	private ClientIdentification clientidentification;
	//private Identification identification;
	
	/**
	 * <h1>Constructor<h1/>
	 * Create an object of class app.
	 * 
	 * @param pId Id of the object.
	 * 
	 * @author ThunderSL94
	 */
	public App(){
		//this.identification = pIdentification;
	}

	public JSONObject getJSONObject() throws JSONException {
		JSONObject jsonRobot = new JSONObject();
		/*jsonRobot.put("identification", identification.getJSONObject());
		jsonRobot.put("identification", identification.getJSONObject());
		jsonRobot.put("active", active);
		jsonRobot.put("position", position.getJSONObject());
		jsonRobot.put("speed", speed);*/

		return jsonRobot;
	}

	public ClientIdentification getClientIdentification() {
		return clientidentification;
	}
	
	

	/*public Identification getIdentification() {
		return identification;
	}*/
}
