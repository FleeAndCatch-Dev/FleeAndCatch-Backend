package flee_and_catch.backend.app;

import flee_and_catch.backend.communication.command.Identification;

public class App {
	private Identification identification;
	
	/**
	 * <h1>Constructor<h1/>
	 * Create an object of class app.
	 * 
	 * @param pId Id of the object.
	 * 
	 * @author ThunderSL94
	 */
	public App(Identification pIdentification){
		this.identification = pIdentification;
	}

	public Identification getIdentification() {
		return identification;
	}
}
