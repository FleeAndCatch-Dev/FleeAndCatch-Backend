import java.io.IOException;

import flee_and_catch.server.Server;

public class Program {

	/**
	 * <h1>Main</h1>
	 * Starts the program.
	 * 
	 * 
	 * @param args
	 * 
	 * @author ThunderSL94
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server;
		try {
			server = new Server();
			server.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
