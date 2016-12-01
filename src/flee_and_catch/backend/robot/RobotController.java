package flee_and_catch.backend.robot;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.Robot;

public final class RobotController {
	private static ArrayList<Robot> robots = new ArrayList<Robot>();

	public static ArrayList<Robot> getRobots() {
		return robots;
	}
}
