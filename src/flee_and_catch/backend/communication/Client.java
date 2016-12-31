package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;

import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class Client {

	private boolean connected;
	private ClientIdentification identification;
	private Device device;
	private Socket socket;
	private BufferedReader bufferedReader;
	private OutputStream outputStream;
	private Interpreter interpreter;
	
	/**
	 * <h1>Constructor</h1>
	 * Creates an object of the class client. The client represents a connection of a component in this application.
	 * 
	 * @param pId Id of the client
	 * @param pConnected Show true, if the thread of the client is open.
	 * @param pSocket The socket of the client.
	 * @param pBufferedReader The reader of the client.
	 * @param pOutputStream The output of the client.
	 * 
	 * @author ThunderSL94
	 */
	public Client(boolean pConnected, ClientIdentification pIdentification,Socket pSocket, BufferedReader pBufferedReader, OutputStream pOutputStream) {
		this.connected = pConnected;
		this.identification = pIdentification;
		this.socket = pSocket;
		this.bufferedReader = pBufferedReader;
		this.outputStream = pOutputStream;
		this.interpreter = new Interpreter(this);
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device pDevice) {
		this.device = pDevice;
	}

	public ClientIdentification getIdentification() {
		return identification;
	}

	public boolean isConnected() {
		return connected;
	}	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}
}
