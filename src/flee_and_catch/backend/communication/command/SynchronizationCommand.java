package flee_and_catch.backend.communication.command;

import java.util.ArrayList;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;
import flee_and_catch.backend.communication.command.szenario.Szenario;

public class SynchronizationCommand extends Command {
	private ArrayList<Szenario> szenarios;
	private ArrayList<Robot> robots;
	
	/**
	 * <h1>Constructor</h1>
	 * Create new synchronization object for json command.
	 * 
	 * @param pId Id as command type.
	 * @param pType Type as command sub type.
	 * @param pClient Client as client object.
	 * @param pRobots Robots as robot list.
	 * 
	 * @author ThunderSL94
	 */
	public SynchronizationCommand(String pId, String pType, ClientIdentification pIdentification, ArrayList<Szenario> pSzenario, ArrayList<Robot> pRobots){
		super(pId, pType, pIdentification);
		this.szenarios = pSzenario;
		this.robots = pRobots;
	}
	
	public ArrayList<Szenario> getSzenarios() {
		return szenarios;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}
}
