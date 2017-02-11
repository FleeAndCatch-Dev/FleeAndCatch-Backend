package flee_and_catch.backend;
import java.io.IOException;

import com.sun.glass.ui.View;

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
				e.printStackTrace();
			}
		}
		else {
			try {
				ViewController.init(false);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ViewController.show();
		
		
		try {
			Server.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
