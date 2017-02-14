package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.view.ViewController;

public final class RobotController {
	private static ArrayList<Robot> robots = new ArrayList<Robot>();
	private static Lock robotsLock = new ReentrantLock();

	public static void changeActive(Robot pRobot, boolean pState){
		for(int i=0; i<RobotController.getRobots().size(); i++){
			if(RobotController.getRobots().get(i).getIdentification().getId() == pRobot.getIdentification().getId()){
				//Check if robots equal
				RobotController.getRobots().get(i).setActive(pState);
			}
		}
	}
	
	public static void addNew(Robot pRobot){
		//Set number of robots in view:
		ViewController.setNumberOfRobots(RobotController.getRobots().size());
		
		robotsLock.lock();
		robots.add(pRobot);
		robotsLock.unlock();
	}
	
	public static void remove(Robot pRobot){
		//Set number of robots in view:
		ViewController.setNumberOfRobots(RobotController.getRobots().size());
		
		robotsLock.lock();
		robots.remove(pRobot);
		robotsLock.unlock();
	}
	
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
}
