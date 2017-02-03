package flee_and_catch.backend.communication.command.device.robot;

public class Position {

	private double x;
	private double y;
	private double orientation;
	
	public Position(double pX, double pY, double pOrientation){
		this.x = pX;
		this.y = pY;
		this.orientation = pOrientation;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getOrientation() {
		return orientation;
	}
}
