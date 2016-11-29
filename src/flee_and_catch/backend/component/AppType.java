package flee_and_catch.backend.component;

public class AppType {
	private String name;
	private Type type;
	
	public enum Type{
		App;
	}
	
	public AppType(Type pType){
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
