package flee_and_catch.backend.communication;

public enum ClientType {
	App, ThreeWheelDriveRobot, FourWheelDriveRobot, ChainDriveRobot;
	
	public String toString(){
		return name().substring(0);
	}
}
