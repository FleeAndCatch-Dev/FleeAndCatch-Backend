package flee_and_catch.backend.communication.command.device.robot;

public class Steering {
	private String direction;
	private String speed;
	
	public Steering(String pDirection, String pSpeed){
		this.direction = Direction.valueOf(pDirection).toString();
		this.speed = Speed.valueOf(pSpeed).toString();	
	}
	public Steering(){
		this.direction = Direction.StraightOn.toString();
		this.speed = Speed.Equal.toString();	
	}

	public String getDirection() {
		return direction;
	}

	public String getSpeed() {
		return speed;
	}
}
