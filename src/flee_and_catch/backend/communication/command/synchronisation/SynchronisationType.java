package flee_and_catch.backend.communication.command.synchronisation;

public class SynchronisationType {
	protected String name;
	protected Type type;
	
	public enum Type{
		GetRobots, SetRobots
	}
	
	public SynchronisationType(Type pType){
		this.name = pType.toString();
		this.type = pType;
	}
}
