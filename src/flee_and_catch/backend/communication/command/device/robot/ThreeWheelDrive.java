package flee_and_catch.backend.communication.command.device.robot;

import flee_and_catch.backend.communication.command.identification.RobotIdentification;

public class ThreeWheelDrive extends Robot {


	public ThreeWheelDrive(RobotIdentification pIdentification, Position pPosition, double pSpeed, double pUltrasonic, double pGyro) {
		super(pIdentification, pPosition, pSpeed, pUltrasonic, pGyro);
	}

}
