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
		sort();
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
	
	public static void update(Robot pRobot){
		robotsLock.lock();
		for(int i=0;i<robots.size();i++){
			if(pRobot.getIdentification().getId() == robots.get(i).getIdentification().getId()){
				robots.set(i, pRobot);
				break;
			}
		}
		robotsLock.unlock();
	}
	
	private static void sort(){
		Robot value;
        for (int i = 0; i < robots.size() - 1; i++) { 
            if (robots.get(i).getIdentification().getId() < robots.get(i + 1).getIdentification().getId()) { 
                continue; 
            } 
            value = robots.get(i); 
            robots.set(i, robots.get(i + 1));
            robots.set(i + 1, value);
            sort(); 
        }
        return;
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
