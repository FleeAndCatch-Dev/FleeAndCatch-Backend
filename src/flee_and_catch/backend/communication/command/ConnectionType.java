package flee_and_catch.backend.communication.command;

public class ConnectionType {
	
	protected String name;
	protected Type type;
	
	public enum Type{
		GetId, SetId, GetType, SetType, Disconnect
	}
	
	public ConnectionType(Type pType){
		this.name = pType.toString();
		this.type = pType;
	}
}
