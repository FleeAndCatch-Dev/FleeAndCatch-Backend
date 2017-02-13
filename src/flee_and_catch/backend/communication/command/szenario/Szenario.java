package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;

public abstract class Szenario {
	protected int id;
	protected String type;
	protected String command;
	protected String mode;
	protected ArrayList<App> apps;
	protected ArrayList<Robot> robots;
	
	public Szenario(int pId, String pType, String pCommand, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots) {
		this.id = pId;
		this.type = pType;
		this.command = pCommand;
		this.mode = pMode;
		this.apps = pApps;
		this.robots = pRobots;
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

	public String getMode() {
		return mode;
	}

	public ArrayList<App> getApps() {
		return apps;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}
}
