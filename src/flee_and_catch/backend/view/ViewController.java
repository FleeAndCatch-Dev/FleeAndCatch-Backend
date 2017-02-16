//### ViewController.java ##################################################################################################################
package flee_and_catch.backend.view;

//### IMPORTS ##############################################################################################################################
import flee_and_catch.backend.view.stage.MainStageController;
import javafx.application.Platform;


public class ViewController {
	
//### STATIC VARIABLES #####################################################################################################################
	
	//Flag that shows if the GUI is active:
	private static Boolean active = null;
	//Main stage of the GUI:
	private static MainStageController mainStage;
	
//### CONSTRUCTORS #########################################################################################################################
	
	private ViewController()  {}
	
//### PUBLIC STATIC METHODS ################################################################################################################
	
	/* init [method]: Method that initialize the view with all his necessary components (must called before use) *//**
	 * 
	 * @param active
	 * @throws InterruptedException
	 */
	public static void init(boolean active) throws InterruptedException {
		
		//Don't rewrite the active variable:
		if(ViewController.active == null) {
			ViewController.active = active;
			ViewController.mainStage = MainStageController.getInstance();
		}
		
		//If the View is activated:
		if(ViewController.active) {
		
			//Starting the JavaFX application:
			App.run();
		
			//Wait until the JavaFX toolkit is initialized:
			while(!App.isInitialized()) {
				Thread.sleep(1);
			}
		
			//Initialize the main stage:
			Platform.runLater(new Runnable() { @Override public void run() {
				ViewController.mainStage.init();
			}});
			
			//Wait until the main stage is initialized:
			while(!MainStageController.isInitialized()) {
				Thread.sleep(1);
			}
		}
	}
	
	/* showView [method]: Method to show the view *//**
	 * 
	 */
	public static void show() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.show();
		}});
	}

	public static void setBackendIPAddress(final String address) {
		//If the View is not activated:
		if(!ViewController.active) { return; }
				
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setBackendIPAddress(address);
		}});
	}
	
	public static void setBackendPort(final String port) {
		//If the View is not activated:
		if(!ViewController.active) { return; }
				
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setBackendPort(port);
		}});
	}
	
	public static void setNumberOfDevices(final int devices) {
		
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setNumberOfDevices(devices);
		}});
	}
	
	public static void setNumberOfApps(final int apps) {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setNumberOfApps(apps);
		}});
	}
	
	public static void setNumberOfRobots(final int robots) {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setNumberOfRobots(robots);
		}});
	}
	
	public static void setStatus(final Status state) {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.setStatus(state);;
		}});
	}
	
	public static void increaseNoOfSyncPackages() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.increaseNoOfSyncPackages();
		}});
		
	}
	
	public static void increaseNoOfControlPackages() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.increaseNoOfControlPackages();
		}});
		
	}
	
	public static void increaseNoOfScenarioPackages() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.increaseNoOfScenarioPackages();
		}});
		
	}
	
	public static void increaseNoOfConnectPackages() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.increaseNoOfConnectPackages();
		}});
		
	}
	
	public static void increaseNoOfDisconnectPackages() {
		//If the View is not activated:
		if(!ViewController.active) { return; }
		
		Platform.runLater(new Runnable() { @Override public void run() {
			ViewController.mainStage.increaseNoOfDisconnectPackages();
		}});
		
	}
	
//##########################################################################################################################################
}
//### EOF ##################################################################################################################################