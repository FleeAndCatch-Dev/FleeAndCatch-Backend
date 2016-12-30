package flee_and_catch.backend.device.robot;

import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.communication.identification.ClientIdentification;
import flee_and_catch.backend.communication.identification.RobotIdentification;

public class ThreeWheelDrive extends Robot {


	public ThreeWheelDrive(ClientIdentification pClientIdentification, RobotIdentification pIdentification, Position pPosition, double pSpeed) {
		super(pClientIdentification, pIdentification, pPosition, pSpeed);
	}

}
