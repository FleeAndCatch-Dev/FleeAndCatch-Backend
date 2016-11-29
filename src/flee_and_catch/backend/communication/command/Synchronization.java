package flee_and_catch.backend.communication.command;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Synchronization extends Command {
	private Identification identification;
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
	public Synchronization(String pId, String pType, Identification pIdentification, ArrayList<Robot> pRobots){
		super(pId, pType);
		this.identification = pIdentification;
		this.robots = pRobots;
	}

	@Override
	/**
	 * <h1>Get command</h1>
	 * Get command as json string.
	 * 
	 * @author ThunderSL94
	 */
	public String getCommand() throws JSONException {
		JSONArray robotarray = new JSONArray();
		for(int i=0; i<robots.size(); i++){
			robotarray.put(robots.get(i).getJSONObject());
		}
		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		command.put("robots", robotarray);
		
		return command.toString();
	}

	public Identification getIdentification() {
		return identification;
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}
}
