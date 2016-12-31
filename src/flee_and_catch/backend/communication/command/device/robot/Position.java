package flee_and_catch.backend.communication.command.device.robot;

import org.json.JSONException;
import org.json.JSONObject;

public class Position {

	private double x;
	private double y;
	private double orientation;
	
	public Position(double pX, double pY, double pOrientation){
		this.x = pX;
		this.y = pY;
		this.orientation = pOrientation;
	}
	
	/**
	 * <h1>Get robot</h1>
	 * Get position as json object.
	 * 
	 * @return
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonPosition = new JSONObject();
		jsonPosition.put("x", x);
		jsonPosition.put("y", y);
		jsonPosition.put("orientation", orientation);

		return jsonPosition;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getOrientation() {
		return orientation;
	}
}
