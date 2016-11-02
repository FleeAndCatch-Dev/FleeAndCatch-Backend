package flee_and_catch.server;

public class RunnableListener implements Runnable {
	
	private Server server;	
	private Thread thread;
	
	/**Constructor
	 * <br>Create a new thread to listen for a client.
	 * 
	 * @param pServer Server object to start the listen function
	 * @author ThunderSL94
	 */
	public RunnableListener(Server pServer) {
		// TODO Auto-generated constructor stub
		server = pServer;
		thread = new Thread(this);
	}
	
	/**Runs the runnable.
	 * @author ThunderSL94
	 */
	public void run() {
		server.listen();
	}
	
	/**Start the new thread.
	 * @author ThunderSL94
	 */
	public void start() {
		thread.start();
	}
}
