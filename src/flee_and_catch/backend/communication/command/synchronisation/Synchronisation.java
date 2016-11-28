package flee_and_catch.backend.communication.command.synchronisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.Command;
import flee_and_catch.backend.communication.command.connection.Client;

public class Synchronisation extends Command {
	private Client client;
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
	public Synchronisation(String pId, String pType, Client pClient, ArrayList<flee_and_catch.backend.robot.Robot> pRobots){
		super(pId, pType);
		this.client = pClient;
		this.robots = new ArrayList<Robot>();
		
		createRobots(pRobots);
	}
	
	/**
	 * <h1>Get command</h1>
	 * Get command as json string.
	 * 
	 * @author ThunderSL94
	 */
	public String GetCommand() throws JSONException{
		JSONArray robotarray = new JSONArray();
		for(int i=0; i<robots.size(); i++){
			robotarray.put(robots.get(i).GetRobot());
		}
		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("client", client.GetClient());
		command.put("robots", robotarray);
		
		return command.toString();
	}

	public Client getClient() {
		return client;
	}
	
	/**
	 * <h1>Create robots</h1>
	 * Create array of robots for json command.
	 * 
	 * @param pRobots Robots from defined robots in the backend.
	 * 
	 * @author ThunderSL94
	 */
	private void createRobots(ArrayList<flee_and_catch.backend.robot.Robot> pRobots){
		for(int i=0; i<pRobots.size(); i++){
			robots.add(new Robot(pRobots.get(i).getId(), pRobots.get(i).getType().toString()));
		}
	}
}
