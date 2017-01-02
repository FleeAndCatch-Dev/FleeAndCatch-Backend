package flee_and_catch.backend;
import java.io.IOException;

import flee_and_catch.backend.communication.Server;

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
			//RobotController.getRobots().add(new ThreeWheelDrive(new RobotIdentification(0, RobotType.ThreeWheelDrive.toString(), RoleType.Undefined.toString()), new Position(0, 0, 0), 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
