package flee_and_catch.server;

import java.net.Socket;

public class RunnableNewClient implements Runnable {
	
	private Server server;	
	private Thread thread;
	private Socket socket;
	
	public RunnableNewClient(Server pServer, Socket pSocket) {
		// TODO Auto-generated constructor stub
		server = pServer;
		socket = pSocket;
		thread = new Thread(this);
	}
	
	public void run() {
		server.addClient(socket);
	}
	
	public void start() {
		thread.start();
	}
}
