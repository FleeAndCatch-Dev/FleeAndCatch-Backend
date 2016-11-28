package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	private int id;
	private String type;
	private String subtype;
	private boolean connected;
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
	public Client(int pId, boolean pConnected, Socket pSocket, BufferedReader pBufferedReader, OutputStream pOutputStream) {
		this.id = pId;
		this.connected = pConnected;
		this.socket = pSocket;
		this.bufferedReader = pBufferedReader;
		this.outputStream = pOutputStream;
		this.interpreter = new Interpreter(this);
	}

	public int getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
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
