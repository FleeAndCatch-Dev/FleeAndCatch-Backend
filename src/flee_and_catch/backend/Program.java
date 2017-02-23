package flee_and_catch.backend;

import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.view.ViewController;

public class Program {

	/**
	 * <h1>Main</h1>
	 * Starts the program.
	 * 
	 * 
	 * @param args Arguments for application
	 * 
	 * @author ThunderSL94
	 */
	public static void main(String[] args) {
		
		//Process parameter:
		if(args.length == 1 && args[0].equals("-v")) {
			try {
				ViewController.init(true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("100 " + e.getMessage());
			}
		}
		else {
			try {
				ViewController.init(false);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("100 " + e.getMessage());
			}
		}
		
		ViewController.show();
		
		Server.open();
	}

}
