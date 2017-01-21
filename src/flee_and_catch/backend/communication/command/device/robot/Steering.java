package flee_and_catch.backend.communication.command.device.robot;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.component.Direction;
import flee_and_catch.backend.communication.command.component.Speed;

public class Steering {
	private String direction;
	private String speed;
	
	public Steering(String pDirection, String pSpeed){
		this.direction = Direction.valueOf(pDirection).toString();
		this.speed = Speed.valueOf(pSpeed).toString();	
	}
	
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonSteering = new JSONObject();
		jsonSteering.put("direction", direction);
		jsonSteering.put("speed", speed);
		
		return jsonSteering;
	}

	public String getDirection() {
		return direction;
	}

	public String getSpeed() {
		return speed;
	}
}
