//### App.java #############################################################################################################################

package flee_and_catch.backend.view;

//### IMPORTS ##############################################################################################################################
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App extends Application {

//### STATIC VARIABLES #####################################################################################################################
	
	public static boolean initialized = false;
	
//### PUBLIC STATIC METHODS ################################################################################################################
	
	public static void run() {
		
		//Create a new thread to avoid that the program is waiting until the JavaFX application is terminated:
        new Thread() {
            @Override
            public void run() {
            	launch(App.class, "");		//Start the JavaFX application!
            }
        }.start();
	}
	
	public static boolean isInitialized() {
		return App.initialized;
	}
	
//### PUBLIC METHODS #######################################################################################################################
	
	//Method that is called before the start-method is called:
	@Override
	public void init() {
		Platform.setImplicitExit(false);
	}
	
	//Method that is called when the JavaFX-Application starts (after init):
	@Override
	public void start(Stage primaryStage) {
		
		App.initialized= true;
	}

	//Method that is called when the JavaFX-Application is shutdown:
	@Override
	public void stop() {
		
		System.out.println("STOP JAVA-FX");
		//Necessary to terminate the whole java application (all threads):
		System.exit(0);
	}
	
//##########################################################################################################################################
}
//### EOF ##################################################################################################################################