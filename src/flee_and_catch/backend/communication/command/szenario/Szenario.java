package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;

public abstract class Szenario {
	protected String szenarioid;
	protected String szenariotype;
	protected ArrayList<App> apps;
	protected ArrayList<Robot> robots;
	
	public Szenario(String pSzenarioId, String pSzenarioType, ArrayList<App> pApps, ArrayList<Robot> pRobots) {
		this.szenarioid = pSzenarioId;
		this.szenariotype = pSzenarioType;
		this.apps = pApps;
		this.robots = pRobots;
	}
	
	public abstract JSONObject getJSONObject() throws JSONException;

	public String getSzenarioid() {
		return szenarioid;
	}

	public String getSzenariotype() {
		return szenariotype;
	}

	public ArrayList<App> getApps() {
		return apps;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}
}
