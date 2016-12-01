package flee_and_catch.backend;
import java.io.IOException;

import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.communication.command.Identification;
import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.communication.command.Robot;
import flee_and_catch.backend.robot.RobotController;

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
			RobotController.getRobots().add(new Robot(new Identification(1, "192.168.0.1", 5000, "Robot", "ThreeWheelDrive"), new Position(0, 0, 0), 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
