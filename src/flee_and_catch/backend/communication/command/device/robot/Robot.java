package flee_and_catch.backend.communication.command.device.robot;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.RobotIdentification;

public class Robot extends Device {
	
	protected RobotIdentification identification;
	protected Position position;
	protected double speed;
	
	public Robot(RobotIdentification pIdentification, Position pPosition, double pSpeed){
		super(false);
		this.identification = pIdentification;
		this.position = pPosition;
		this.speed = pSpeed;
	}
	
	public Robot(RobotIdentification pIdentification, boolean pActive, Position pPosition, double pSpeed){
		super(pActive);
		this.identification = pIdentification;
		this.position = pPosition;
		this.speed = pSpeed;
	}
	
	public Robot(Robot pRobot){
		super(pRobot.isActive());
		this.identification = pRobot.getIdentification();
		this.position = pRobot.getPosition();
		this.speed = pRobot.getSpeed();
	}

	public RobotIdentification getIdentification() {
		return identification;
	}

	public Position getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}
}
