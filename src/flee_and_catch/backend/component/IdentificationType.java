package flee_and_catch.backend.component;

public class IdentificationType {
	
	private String name;
	private Typ typ;
	
	public enum Typ{
		App, Robot;
	}
	
	public IdentificationType(Typ pTyp){
		this.name = pTyp.toString();
		this.typ = pTyp;
	}

	public String getName() {
		return name;
	}

	public Typ getTyp() {
		return typ;
	}
}
