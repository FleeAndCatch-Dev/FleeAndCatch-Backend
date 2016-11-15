package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

	private int id;
	private String type;
	private boolean opened;
	private Socket socket;
	private Thread thread;
	private BufferedReader reader;
	private DataOutputStream outputStream;
	private Interpreter interpreter;
	private ArrayList<String> commandList;
	
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
	public Client(int pId, boolean pOpened, Socket pSocket, Thread pThread) {
		this.id = pId;
		this.opened = pOpened;
		this.socket = pSocket;
		this.thread = pThread;
		this.commandList = new ArrayList<String>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened= opened;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	public DataOutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ArrayList<String> getCommandList() {
		return commandList;
	}
	public void setCommandList(ArrayList<String> commandList) {
		this.commandList = commandList;
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}
	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}	
	
}
