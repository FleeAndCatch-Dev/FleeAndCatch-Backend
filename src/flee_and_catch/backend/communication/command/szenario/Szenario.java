package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public abstract class Szenario {
	protected int id;
	protected String type;
	protected String command;
	protected String mode;
	protected ArrayList<App> apps;
	protected ArrayList<Robot> robots;
	private Steering steering;
	
	public Szenario(int pId, String pType, String pCommand, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering pSteering) {
		this.id = pId;
		this.type = pType;
		this.command = pCommand;
		this.mode = pMode;
		this.apps = pApps;
		this.robots = pRobots;
		this.steering = pSteering;
	}	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public String getMode() {
		return mode;
	}

	public ArrayList<App> getApps() {
		return apps;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}
	public void setRobots(ArrayList<Robot> robots) {
		this.robots = robots;
	}
	public Steering getSteering() {
		return steering;
	}
}
