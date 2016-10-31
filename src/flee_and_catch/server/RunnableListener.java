package flee_and_catch.server;

public class RunnableListener implements Runnable {
	
	private Server server;	
	private Thread thread;
	
	public RunnableListener(Server pServer) {
		// TODO Auto-generated constructor stub
		server = pServer;
		thread = new Thread(this);
	}
	
	public void run() {
		server.listen();
	}
	
	public void start() {
		thread.start();
	}
}
