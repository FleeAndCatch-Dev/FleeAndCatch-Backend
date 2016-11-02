package flee_and_catch.server;

import java.net.Socket;

public class RunnableNewClient implements Runnable {
	
	private Server server;	
	private Thread thread;
	private Socket socket;
	
	/**Constructor
	 * <br>Create a new thread for listen commands at a new client.
	 * 
	 * @param pServer Server object to runs the function
	 * @param pSocket Socket for the new thread
	 * @author ThunderSL94
	 */
	public RunnableNewClient(Server pServer, Socket pSocket) {
		// TODO Auto-generated constructor stub
		server = pServer;
		socket = pSocket;
		thread = new Thread(this);
	}
	
	/**Runs the runnable.
	 * @author ThunderSL94
	 */
	public void run() {
		server.addClient(socket);
	}
	
	/**Start the new thread.
	 * @author ThunderSL94
	 */
	public void start() {
		thread.start();
	}
}
