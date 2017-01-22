package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public class Control extends Szenario {
	private Steering steering;

	public Control(String pSzenarioId, String pSzenarioType, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering steering) {
		super(pSzenarioId, pSzenarioType, pApps, pRobots);
		this.steering = steering;
	}
	
	public Control(Control pControl) {
		super(pControl.getSzenarioid(), pControl.getSzenariotype(), pControl.getApps(), pControl.getRobots());
		this.steering = pControl.getSteering();
	}

	@Override
	public JSONObject getJSONObject() throws JSONException {
		JSONArray apparray = new JSONArray();
		for(int i=0; i<apps.size(); i++){
			apparray.put(apps.get(i).getJSONObject());
		}
		JSONArray robotarray = new JSONArray();
		for(int i=0; i<robots.size(); i++){
			robotarray.put(robots.get(i).getJSONObject());
		}
		
		JSONObject jsonControl = new JSONObject();
		jsonControl.put("szenarioid", szenarioid);
		jsonControl.put("szenariotype", szenariotype);
		jsonControl.put("apps", apparray);
		jsonControl.put("robots", robotarray);
		jsonControl.put("steering", steering.getJSONObject());
		
		return jsonControl;
	}

	public Steering getSteering() {
		return steering;
	}
}
