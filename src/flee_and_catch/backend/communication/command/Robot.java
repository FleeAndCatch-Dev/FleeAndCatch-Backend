package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

public class Robot {
	private Identification identification;
	private Position position;
	private double speed;
	
	public Robot(Identification pIdentification, Position pPosition, double pSpeed){
		this.identification = pIdentification;
		this.position = pPosition;
		this.speed = pSpeed;
	}

	/**
	 * <h1>Get robot</h1>
	 * Get robot as json object.
	 * 
	 * @return
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonRobot = new JSONObject();
		jsonRobot.put("identification", identification.getJSONObject());
		jsonRobot.put("position", position.getJSONObject());
		jsonRobot.put("speed", speed);

		return jsonRobot;
	}
	
	public Identification getIdentification() {
		return identification;
	}

	public Position getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}
}
