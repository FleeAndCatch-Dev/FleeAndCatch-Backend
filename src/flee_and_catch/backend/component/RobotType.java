package flee_and_catch.backend.component;

public class RobotType {
	private String name;
	private Type type;
	
	public enum Type{
		ThreeWheelDrive, FourWheelDrive, ChainDrive;
	}
	
	public RobotType(Type pType){
		this.name = pType.toString();
		this.type = pType;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
}
