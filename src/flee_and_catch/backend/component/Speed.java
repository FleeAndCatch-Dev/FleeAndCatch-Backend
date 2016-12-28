package flee_and_catch.backend.component;

public enum Speed {
	
	Slower (-1),
	Equal (0),
	Faster (1);
	
	private int value;
	
	private Speed(int value) {
		this.value = value;
	}
	
	public static Speed get(int value) {
		
		switch(value) {
			case -1:
				return Speed.Slower;
			case 0:
				return Speed.Equal;
			case 1:
				return Speed.Slower;
			default:
				return null;
		}
		
	}

	public int getValue() {
		return value;
	}
}
