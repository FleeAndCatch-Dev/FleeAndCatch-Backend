package flee_and_catch.backend.communication.command;

import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class ControlCommand extends Command {
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
	public ControlCommand(String pId, String pType, ClientIdentification pIdentification, Robot pRobot, Steering pSteering) {
		super(pId, pType, pIdentification);
		this.steering = pSteering;
		this.robot = pRobot;
	}

	public Robot getRobot() {
		return robot;
	}

	public Steering getSteering() {
		return steering;
	}
}
