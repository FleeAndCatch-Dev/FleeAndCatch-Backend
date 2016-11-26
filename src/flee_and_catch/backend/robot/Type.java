package flee_and_catch.backend.robot;

public enum Type {
	
	ThreeWheelDrive ("ThreeWheelDrive");
	
	private String name;
	
	private Type(String pName){
		this.name = pName;
	}
	
	public String getName() {
		return name;
	}
}