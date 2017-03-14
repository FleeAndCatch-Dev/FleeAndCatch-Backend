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
		robotsLock.lock();
		for(int i=0; i<RobotController.getRobots().size(); i++){
			if(RobotController.getRobots().get(i).getIdentification().getId() == pRobot.getIdentification().getId()){
				//Check if robots equal
				RobotController.getRobots().get(i).setActive(pState);
			}
		}
		robotsLock.unlock();
	}
	
	public static void addNew(Robot pRobot){		
		robotsLock.lock();
		robots.add(pRobot);
		robotsLock.unlock();
		
		//Set number of robots in view:
		//ViewController.setNumberOfDevices(Server.getClients().size());
		ViewController.setNumberOfRobots(RobotController.getRobots().size());
	}
	
	public static void remove(Robot pRobot){		
		robotsLock.lock();
		robots.remove(pRobot);
		robotsLock.unlock();
		
		//Set number of robots in view:
		//ViewController.setNumberOfDevices(Server.getClients().size());
		ViewController.setNumberOfRobots(RobotController.getRobots().size());
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
