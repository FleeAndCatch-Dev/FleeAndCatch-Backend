package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public class Control extends Szenario {
	private Steering steering;

	public Control(String pSzenarioId, String pSzenarioType, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering steering) {
		super(pSzenarioId, pSzenarioType, pMode, pApps, pRobots);
		this.steering = steering;
	}
	
	public Control(Control pControl) {
		super(pControl.getSzenarioid(), pControl.getSzenariotype(), pControl.getMode(), pControl.getApps(), pControl.getRobots());
		this.steering = pControl.getSteering();
	}

	public Steering getSteering() {
		return steering;
	}
}
