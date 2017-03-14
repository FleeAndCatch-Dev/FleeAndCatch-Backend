package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public class Follow extends Szenario {
	
	public Follow(int pId, String pType, String pCommand, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering pSteering) {
		super(pId, pType, pCommand, pMode, pApps, pRobots, pSteering);
	}
	
	public Follow(Follow pFollow) {
		super(pFollow.getId(), pFollow.getType(), pFollow.getCommand(), pFollow.getMode(), pFollow.getApps(), pFollow.getRobots(), pFollow.getSteering());
	}
}
