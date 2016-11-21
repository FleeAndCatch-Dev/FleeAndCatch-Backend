package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	private int id;
	private ClientType type;
	private boolean connected;
	private Socket socket;
	private BufferedReader bufferedReader;
	private OutputStream outputStream;
	private Interpreter interpreter;
	
	/**
	 * <h1>Constructor</h1>
	 * Creates an object of the class client. The client represents a connection of a component in this application.
	 * 
	 * 
	 * @param pId Id of the client
	 * @param pOpened Show true, if the thread of the client is open.
	 * @param pSocket The socket of the client.
	 * @param pThread The thread of the client.
	 * 
	 * @author ThunderSL94
	 */
	public Client(int pId, ClientType pType, boolean pConnected, Socket pSocket, BufferedReader pBufferedReader, OutputStream pOutputStream, Interpreter pInterpreter) {
		this.id = pId;
		this.type = pType;
		this.connected = pConnected;
		this.socket = pSocket;
		this.bufferedReader = pBufferedReader;
		this.outputStream = pOutputStream;
		this.interpreter = pInterpreter;
	}

	public int getId() {
		return id;
	}
	
	public ClientType getType() {
		return type;
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
