package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class Control extends Command {
	private Robot robot;
	private Steering steering;
	/**
	 * <h1>Constructor</h1>
	 * Create new connection object for json command.
	 * 
	 * @param pId
	 * @param pType
	 * @param pClient
	 * 
	 * @author ThunderSL94
	 */
	public Control(String pId, String pType, ClientIdentification pIdentification, Robot pRobot, Steering pSteering) {
		super(pId, pType, pIdentification);
		this.robot = pRobot;
		this.steering = pSteering;
	}
	
	/**
	 * <h1>Get command</h1>
	 * Get the command as a json string.
	 * 
	 * @return Json command as string.
	 * 
	 * @author ThunderSL94
	 */
	public String getCommand() throws JSONException{
		JSONObject control = new JSONObject();
		control.put("robot", robot.getJSONObject());
		control.put("steering", steering.getJSONObject());
		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		command.put("control", control);
		
		return command.toString();
	}

	public Robot getRobot() {
		return robot;
	}

	public Steering getSteering() {
		return steering;
	}
}
