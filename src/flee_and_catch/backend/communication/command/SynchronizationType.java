package flee_and_catch.backend.communication.command;

public class SynchronizationType {
	protected String name;
	protected Type type;
	
	public enum Type{
		GetRobots, SetRobots
	}
	
	public SynchronizationType(Type pType){
		this.name = pType.toString();
		this.type = pType;
	}
}
