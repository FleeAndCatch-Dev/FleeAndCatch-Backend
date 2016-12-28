package flee_and_catch.backend;
import java.io.IOException;

import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.communication.command.Identification;
import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.component.IdentificationType;
import flee_and_catch.backend.component.RobotType;
import flee_and_catch.backend.robot.RobotController;
import flee_and_catch.backend.robot.ThreeWheelDrive;

public class Program {

	/**
	 * <h1>Main</h1>
	 * Starts the program.
	 * 
	 * 
	 * @param args Arguments for application
	 * 
	 * @author ThunderSL94
	 */
	public static void main(String[] args) {
		try {
			Server.open();
			RobotController.getRobots().add(new ThreeWheelDrive(new Identification(Server.generateNewClientId(), "192.168.1.2", 5000, IdentificationType.Robot.toString(), RobotType.ThreeWheelDrive.toString()), new Position(0, 0, 0), 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
