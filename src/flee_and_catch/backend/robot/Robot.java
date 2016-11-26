package flee_and_catch.backend.robot;

public abstract class Robot {

	protected int id;
	protected Type type;
	
	protected Robot(int pId, String pType){
		this.id = pId;
		this.type = Type.valueOf(pType);
	}

	public int getId() {
		return id;
	}

	public Type getType() {
		return type;
	}
}
