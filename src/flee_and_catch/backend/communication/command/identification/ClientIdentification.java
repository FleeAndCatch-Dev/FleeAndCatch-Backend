package flee_and_catch.backend.communication.command.identification;

public class ClientIdentification extends Identification {

	private String address;
	private int port;
	
	public ClientIdentification(int pId, String pAddress, int pPort, String pType){
		this.id = pId;
		this.address = pAddress;
		this.port = pPort;
		this.type = IdentificationType.valueOf(pType).toString();
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
}
