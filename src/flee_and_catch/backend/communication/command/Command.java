package flee_and_catch.backend.communication.command;

import org.json.JSONException;

public abstract class Command {
	protected String id;
	protected String type;
	protected String apiid;
	protected String errorhandling;
	
	/**
	 * <h1>Constructor</h1>
	 * Create a new command object.
	 * 
	 * @param pId Id as command type.
	 * @param pType Type as sub command type.
	 * 
	 * @author ThunderSL94
	 */
	protected Command(String pId, String pType){
		this.id = pId;
		this.type = pType;
		this.errorhandling = "ignoreerrors";
		this.apiid = "@@fleeandcatch@@";
	}
	
	public abstract String GetCommand() throws JSONException;

	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}

	public String getApiid() {
		return apiid;
	}

	public String getErrorhandling() {
		return errorhandling;
	}
}
