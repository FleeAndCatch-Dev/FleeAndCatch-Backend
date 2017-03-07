package flee_and_catch.backend.communication.command.device.robot;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.RobotIdentification;

public class Robot extends Device {
	
	protected RobotIdentification identification;
	protected Position position;
	protected double speed;
	protected String ultrasonic;
	protected String gyro;
	
	public Robot(RobotIdentification pIdentification, Position pPosition, double pSpeed, double pUltrasonic, double pGyro){
		super(false);
		this.identification = pIdentification;
		this.position = pPosition;
		this.speed = pSpeed;
		this.ultrasonic = Double.toString(pUltrasonic);
		this.gyro = Double.toString(pGyro);
	}
	
	public Robot(RobotIdentification pIdentification, boolean pActive, Position pPosition, double pSpeed, double pUltrasonic, double pGyro){
		super(pActive);
		this.identification = pIdentification;
		this.position = pPosition;
		this.speed = pSpeed;
		this.ultrasonic = Double.toString(pUltrasonic);
		this.gyro = Double.toString(pGyro);
	}
	
	public Robot(Robot pRobot){
		super(pRobot.isActive());
		this.identification = pRobot.getIdentification();
		this.position = pRobot.getPosition();
		this.speed = pRobot.getSpeed();
		this.ultrasonic =  pRobot.getUltrasonic();
		this.gyro =  pRobot.getGyro();
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

	public String getUltrasonic() {
		return ultrasonic;
	}

	public String getGyro() {
		return gyro;
	}
}
