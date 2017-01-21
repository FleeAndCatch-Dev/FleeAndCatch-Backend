package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.device.robot.Robot;

public final class RobotController {
	private static ArrayList<Robot> robots = new ArrayList<Robot>();
	private static Lock robotsLock = new ReentrantLock();

	public static ArrayList<Robot> getRobots() {
		robotsLock.lock();
		ArrayList<Robot> robotList = new ArrayList<Robot>(robots);
		robotsLock.unlock();
		return robotList;
	}
	
	
	public static void setRobots(ArrayList<Robot> robots) {
		robotsLock.lock();
		RobotController.robots = robots;
		robotsLock.unlock();
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
