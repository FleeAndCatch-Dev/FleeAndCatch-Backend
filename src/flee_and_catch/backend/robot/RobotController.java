package flee_and_catch.backend.robot;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.Robot;

public final class RobotController {
	private static ArrayList<Robot> robots = new ArrayList<Robot>();

	public static ArrayList<Robot> getRobots() {
		return robots;
	}
	
	public static void changeActive(Robot pRobot, boolean pState){
		for(int i=0; i<RobotController.getRobots().size(); i++){
			if(RobotController.getRobots().get(i).getIdentification().getId() == pRobot.getIdentification().getId()){
				//Check if robots equal
				RobotController.getRobots().get(i).setActive(pState);
			}
		}
	}
}
