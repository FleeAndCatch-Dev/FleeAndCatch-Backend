package flee_and_catch.backend.communication;

public enum Type {
	App ("App"), Robot ("Robot");
	
	private String name;
	
	private Type(String pName){
		this.name = pName;
	}

	public String getName() {
		return name;
	}
}
