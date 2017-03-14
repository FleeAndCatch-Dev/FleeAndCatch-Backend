package flee_and_catch.backend.communication.command.device.app;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.AppIdentification;

public class App extends Device {
	private AppIdentification identification;
	
	/**
	 * <h1>Constructor<h1/>
	 * Create an object of class app.
	 * 
	 * @param pId Id of the object.
	 * 
	 * @author ThunderSL94
	 */
	public App(AppIdentification pIdentification){
		super(false);
		this.identification = pIdentification;
	}
	
	public App(AppIdentification pIdentification, boolean pActive){
		super(pActive);
		this.identification = pIdentification;
	}
	
	public App(App pApp){
		super(pApp.isActive());
		this.identification = pApp.getIdentification();
	}

	public AppIdentification getIdentification() {
		return identification;
	}
}
