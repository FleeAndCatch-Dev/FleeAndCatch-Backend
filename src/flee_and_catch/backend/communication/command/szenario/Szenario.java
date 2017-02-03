package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;

public abstract class Szenario {
	protected String szenarioid;
	protected String szenariotype;
	protected String mode;
	protected ArrayList<App> apps;
	protected ArrayList<Robot> robots;
	
	public Szenario(String pSzenarioId, String pSzenarioType, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots) {
		this.szenarioid = pSzenarioId;
		this.szenariotype = pSzenarioType;
		this.mode = pMode;
		this.apps = pApps;
		this.robots = pRobots;
	}

	public String getSzenarioid() {
		return szenarioid;
	}

	public String getSzenariotype() {
		return szenariotype;
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
