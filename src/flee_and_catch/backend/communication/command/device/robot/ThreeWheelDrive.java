package flee_and_catch.backend.communication.command.device.robot;

import flee_and_catch.backend.communication.command.identification.ClientIdentification;
import flee_and_catch.backend.communication.command.identification.RobotIdentification;

public class ThreeWheelDrive extends Robot {


	public ThreeWheelDrive(ClientIdentification pClientIdentification, RobotIdentification pIdentification, Position pPosition, double pSpeed) {
		super(pClientIdentification, pIdentification, pPosition, pSpeed);
	}

}
