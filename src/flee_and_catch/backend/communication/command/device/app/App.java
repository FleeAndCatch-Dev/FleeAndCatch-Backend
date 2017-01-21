package flee_and_catch.backend.communication.command.device.app;


import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.AppIdentification;

public class App implements Device {
	private AppIdentification identification;
	private boolean active;
	//private Identification identification;
	
	/**
	 * <h1>Constructor<h1/>
	 * Create an object of class app.
	 * 
	 * @param pId Id of the object.
	 * 
	 * @author ThunderSL94
	 */
	public App(AppIdentification pIdentification){
		this.identification = pIdentification;
		this.active = false;
	}
	
	public App(AppIdentification pIdentification, boolean pActive){
		this.identification = pIdentification;
		this.active = pActive;
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		JSONObject jsonApp = new JSONObject();
		jsonApp.put("identification", identification.getJSONObject());
		jsonApp.put("active", active);

		return jsonApp;
	}

	public AppIdentification getIdentification() {
		return identification;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
