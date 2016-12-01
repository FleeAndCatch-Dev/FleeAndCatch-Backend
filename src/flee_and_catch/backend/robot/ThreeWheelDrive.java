package flee_and_catch.backend.robot;

import flee_and_catch.backend.communication.command.Identification;
import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.communication.command.Robot;
import flee_and_catch.backend.component.RobotType;
import flee_and_catch.backend.component.IdentificationType;

public class ThreeWheelDrive extends Robot {

	public ThreeWheelDrive(Identification pIdentification, Position pPosition, double pSpeed) {
		super(pIdentification, pPosition, pSpeed);
	}

}
