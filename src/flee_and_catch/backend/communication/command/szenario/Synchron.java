package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public class Synchron extends Szenario {
	private Steering steering;

	public Synchron(int pId, String pType, String pCommand, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering steering) {
		super(pId, pType, pCommand, pMode, pApps, pRobots);
		this.steering = steering;
	}
	
	public Synchron(Synchron pSynchron) {
		super(pSynchron.getId(), pSynchron.getType(), pSynchron.getCommand(), pSynchron.getMode(), pSynchron.getApps(), pSynchron.getRobots());
		this.steering = pSynchron.getSteering();
	}

	public Steering getSteering() {
		return steering;
	}
}
