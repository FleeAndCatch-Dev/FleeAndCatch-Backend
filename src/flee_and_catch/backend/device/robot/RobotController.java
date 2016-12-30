package flee_and_catch.backend.device.robot;

import java.util.ArrayList;

public final class RobotController {
	private static ArrayList<Robot> robots = new ArrayList<Robot>();

	public static ArrayList<Robot> getRobots() {
		return robots;
	}
	
	public static void changeActive(Robot pRobot, boolean pState){
		for(int i=0; i<RobotController.getRobots().size(); i++){
			if(RobotController.getRobots().get(i).getClientIdentification().getId() == pRobot.getClientIdentification().getId()){
				//Check if robots equal
				RobotController.getRobots().get(i).setActive(pState);
			}
		}
	}
}
