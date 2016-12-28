package flee_and_catch.backend.component;

public enum Direction {

	Left (-1),
	StraightOn (0),
	Right (1);
	
	private int value;
	
	private Direction(int value) {
		this.value = value;
	}
	
	public static Direction get(int value) {
		
		switch(value) {
			case -1:
				return Direction.Left;
			case 0:
				return Direction.StraightOn;
			case 1:
				return Direction.Right;
			default:
				return null;
		}
		
	}

	public int getValue() {
		return value;
	}
	
}
