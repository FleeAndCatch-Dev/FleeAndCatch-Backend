package flee_and_catch.backend.communication.command.device.robot;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.component.Direction;
import flee_and_catch.backend.communication.command.component.Speed;

public class Steering {
	private Direction direction;
	private Speed speed;
	
	public Steering(int pDirection, int pSpeed){
		this.direction = Direction.get(pDirection);
		this.speed = Speed.get(pSpeed);	
	}
	
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonSteering = new JSONObject();
		jsonSteering.put("direction", direction.getValue());
		jsonSteering.put("speed", speed.getValue());
		
		return jsonSteering;
	}

	public Direction getDirection() {
		return direction;
	}

	public Speed getSpeed() {
		return speed;
	}
}
