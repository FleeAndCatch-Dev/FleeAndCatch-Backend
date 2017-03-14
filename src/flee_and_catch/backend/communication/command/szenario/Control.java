package flee_and_catch.backend.communication.command.szenario;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;

public class Control extends Szenario {
	
	public Control(int pId, String pType, String pCommand, String pMode, ArrayList<App> pApps, ArrayList<Robot> pRobots, Steering pSteering) {
		super(pId, pType, pCommand, pMode, pApps, pRobots, pSteering);
	}
	
	public Control(Control pControl) {
		super(pControl.getId(), pControl.getType(), pControl.getCommand(), pControl.getMode(), pControl.getApps(), pControl.getRobots(), pControl.getSteering());
	}
}
