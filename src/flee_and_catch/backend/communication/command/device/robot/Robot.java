package flee_and_catch.backend.communication.command.device.robot;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.RobotIdentification;

public class Robot implements Device {
	
	private RobotIdentification identification;
	private boolean active;
	private Position position;
	private double speed;
	
	public Robot(RobotIdentification pIdentification, Position pPosition, double pSpeed){
		this.identification = pIdentification;
		this.active = false;
		this.position = pPosition;
		this.speed = pSpeed;
	}
	
	public Robot(RobotIdentification pIdentification, boolean pActive, Position pPosition, double pSpeed){
		this.identification = pIdentification;
		this.active = pActive;
		this.position = pPosition;
		this.speed = pSpeed;
	}
	
	public Robot(Robot pRobot){
		this.identification = pRobot.getIdentification();
		this.active = pRobot.isActive();
		this.position = pRobot.getPosition();
		this.speed = pRobot.getSpeed();
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
	@Override
	public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonRobot = new JSONObject();
		jsonRobot.put("identification", identification.getJSONObject());
		jsonRobot.put("active", active);
		jsonRobot.put("position", position.getJSONObject());
		jsonRobot.put("speed", speed);

		return jsonRobot;
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
