package flee_and_catch.backend.device.robot;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.communication.identification.ClientIdentification;
import flee_and_catch.backend.communication.identification.RobotIdentification;
import flee_and_catch.backend.device.Device;

public class Robot implements Device {
	
	private ClientIdentification clientidentification;
	private RobotIdentification identification;
	private boolean active;
	private Position position;
	private double speed;
	
	public Robot(ClientIdentification pClientIdentification, RobotIdentification pIdentification, Position pPosition, double pSpeed){
		this.clientidentification = pClientIdentification;
		this.identification = pIdentification;
		this.active = false;
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
		jsonRobot.put("clientidentification", clientidentification.getJSONObject());
		jsonRobot.put("identification", identification.getJSONObject());
		jsonRobot.put("active", active);
		jsonRobot.put("position", position.getJSONObject());
		jsonRobot.put("speed", speed);

		return jsonRobot;
	}

	public ClientIdentification getClientIdentification() {
		return clientidentification;
	}

	public RobotIdentification getIdentification() {
		return identification;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public Position getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}
}
