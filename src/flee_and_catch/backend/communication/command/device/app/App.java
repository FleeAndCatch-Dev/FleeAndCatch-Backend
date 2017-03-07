package flee_and_catch.backend.communication.command.device.app;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.AppIdentification;

public class App extends Device {
	private AppIdentification identification;
	private int robotid;
	
	/**
	 * <h1>Constructor<h1/>
	 * Create an object of class app.
	 * 
	 * @param pId Id of the object.
	 * 
	 * @author ThunderSL94
	 */
	public App(AppIdentification pIdentification, int pRobotId){
		super(false);
		this.identification = pIdentification;
		this.robotid = pRobotId;
	}
	
	public App(AppIdentification pIdentification, boolean pActive, int pRobotId){
		super(pActive);
		this.identification = pIdentification;
		this.robotid = pRobotId;
	}
	
	public App(App pApp){
		super(pApp.isActive());
		this.identification = pApp.getIdentification();
		this.robotid = pApp.getRobotId();
	}

	public AppIdentification getIdentification() {
		return identification;
	}
	
	public int getRobotId(){
		return robotid;
	}
	public void setRobotId(int robotid){
		this.robotid = robotid;
	}
}
