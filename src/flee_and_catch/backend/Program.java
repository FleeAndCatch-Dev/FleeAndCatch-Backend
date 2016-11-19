package flee_and_catch.backend;
import java.io.IOException;

import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.exception.OpenConnection;

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
		try {
			Server server = new Server();
			server.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpenConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
