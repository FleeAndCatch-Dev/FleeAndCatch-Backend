package flee_and_catch.backend.communication.command;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class ConnectionCommand extends Command {	
	private Device device;
	
	/**
	 * <h1>Constructor</h1>
	 * Create new connection object for json command.
	 * 
	 * @param pId
	 * @param pType
	 * @param pClient
	 * 
	 * @author ThunderSL94
	 */
	public ConnectionCommand(String pId, String pType, ClientIdentification pIdentification, Device pDevice){
		super(pId, pType, pIdentification);
		this.device = pDevice;
	}

	public Device getDevice() {
		return device;
	}	
}
