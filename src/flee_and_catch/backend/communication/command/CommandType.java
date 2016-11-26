package flee_and_catch.backend.communication.command;

public class CommandType {
	protected String name;
	protected Type type;
	
	public enum Type{
		Connection, Synchronisation
	}
	
	public CommandType(Type pType){
		this.name = pType.toString();
		this.type = pType;
	}
}
