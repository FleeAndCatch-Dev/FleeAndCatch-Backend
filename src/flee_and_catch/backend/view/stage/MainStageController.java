//### ViewController.java ##################################################################################################################

package flee_and_catch.backend.view.stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import flee_and_catch.backend.view.Status;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.WindowEvent;

//### IMPORTS ##############################################################################################################################

public class MainStageController {

//### STATIC VARIABLES #####################################################################################################################
	
	//Singleton reference:
	private static MainStageController instance;
	//Shows if GUI is initialized:
	private static boolean inizialized;
	
//### ATTRIBUTES ###########################################################################################################################
	
	private	MainStage 			view;		//View!
	private MainStageResources 	res;		//Resources for the view
	private ActionEventHandler 	aeh;		//Handler for action events (e.g. clicks) on the view!
	private MouseEventHandler   meh;		//Handler for mouse events (e.g. mouse movement) on the view!
	private KeyEventHandler 	keh;		//Handler for key events (e.g. keypress) on view!
	private WindowEventHandler 	weh;		//Handler for window events (e.g. close over cross) on view!
	
	//Threads:
	private ShowTimeThread stt;
	private ShowStatusThread sst;
	
	//Helping Variables:
	private ArrayList<Status> proStates;				//Saves the states of the program!
	private Status curState;				//Saves the status that is processed by the view!
	
//### CONSTRUCTORS #########################################################################################################################	
	
	private MainStageController() {};
	
//### INITIAL METHODS ######################################################################################################################	
	
	/* init() [method]: Initialize the main stage and all its components (must called before use)  */
	public void init() {

		//Initialize the resources for the view:
		this.res = new MainStageResources();
		//Initialize the action event handler for the view:
		this.aeh = new ActionEventHandler();
		//Initialize the mouse event handler for the view:
		this.meh = new MouseEventHandler();
		//Initialize the key event handler for the view:
		this.keh = new KeyEventHandler();
		//Initialize the window event handler for the view:
		this.weh = new WindowEventHandler();
		//Initialize the view:
		this.view = new MainStage(res, aeh, meh, keh, weh);

		this.stt = new ShowTimeThread();
		this.sst = new ShowStatusThread();
		
		this.proStates = new ArrayList<Status>();
		this.curState = Status.Nothing;
		
		//Set up the bindings for view components:
		this.initBindings();
		
		//Set flag that GUI is initialized:
		MainStageController.inizialized = true;
	}
	
	
	private void initBindings() {
		
	}
	
//### EVENT HANDLER ########################################################################################################################
	
	/* ActionEventHandler [class]: Class that implements an event handler that handles all action events of the view *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class ActionEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			
			//Read out the object that triggered the event:
			Object src = event.getSource();
		
			if(src == MainStageController.this.view.mniExit) {
				MainStageController.this.showCloseProgramDialog();
			}
			else if(src == MainStageController.this.view.rttConnected) {
				MainStageController.this.curState = Status.Nothing;
				MainStageController.this.proStates.remove(0);
			}
			else if(src == MainStageController.this.view.rttDisconnected) {
				MainStageController.this.curState = Status.Nothing;
				MainStageController.this.proStates.remove(0);
			}
		}
		
	}
	
	/* MouseEventHandler [class]: Class that implements an event handler that handles all mouse events of the view *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class MouseEventHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/* KeyEventHandler [class]: Class that implements an event handler that handles all key events of the view *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class KeyEventHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

	/* WindowEventHandler [class]: Class that implements an event handler that handles all window events of the view *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class WindowEventHandler implements EventHandler<WindowEvent> {

		@Override
		public void handle(WindowEvent event) {
			
			//Stops the further processing of the event:
			event.consume();
			MainStageController.this.showCloseProgramDialog();
			
		}
		
	}
	
//### INNER CLASSES ########################################################################################################################
	
	/* ShowTimeThread [class]: Thread that updates the time in the status bar of the stage *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class ShowTimeThread extends Thread {
		
		@Override
		public void run() {
			
			//Run forever:
			while(true) {
				
				//
				Platform.runLater(new Runnable() { @Override public void run() {
					Date date = new Date();
					String time = date.toString().substring(11, 19);
					MainStageController.this.view.lblStatusBarTime.setText(time);
				}});
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class ShowStatusThread extends Thread {
		
		@Override
		public void run() {
			
			//Run forever:
			while(true) {
				
				Platform.runLater(new Runnable() { @Override public void run() {
					
					//Is actually no state in processing and a state there to process:
					if((MainStageController.this.curState == Status.Nothing || MainStageController.this.curState == Status.Waiting)
						&& MainStageController.this.proStates.size() > 0) {
					
						Status curStatus = MainStageController.this.proStates.get(0);
					
						//If the view is ready with the processing of the status:
						switch(curStatus) {
							case Nothing:
								break;
							case Connected:
								MainStageController.this.setStatusToDeviceConnected();
								break;
							case Disconnected:
								MainStageController.this.setStatusToDeviceDisconnected();
								break;
							case Waiting:
								MainStageController.this.setStatusToWaitingForDevices();
								break;
							default:
								break;
						}
					}
				}});
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
//### PRIVATE METHODS ######################################################################################################################
	
	private void showCloseProgramDialog() {
		
		//Create dialog:
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		//Create the dialog buttons:
		ButtonType bttApply = new ButtonType(this.res.cpdBtnApplyText);
		ButtonType bttCancel = new ButtonType(this.res.cpdBtnCancelText);
		
		//Set properties of the dialog:
		alert.setTitle(this.res.cpdTitle);
		alert.setHeaderText(this.res.cpdHeaderText);
		alert.setContentText(this.res.cpdContentText);
		alert.getButtonTypes().setAll(bttApply, bttCancel);
		
		//Show dialog and read out the result:
		Optional<ButtonType> result = alert.showAndWait();
		
		//If the user confirm the game abortion:
		if (result.get() == bttApply){
		    Platform.exit();
		}
	}
	
//### PUBLIC STATIC METHODS ################################################################################################################
	
	/* getInstance [method]: Fabric method to (create and) returns the singleton object of the class */
	public static synchronized MainStageController getInstance() {
		//Create a view controller object only once:
		if(MainStageController.instance == null) {
			MainStageController.instance = new MainStageController();
		}
		return MainStageController.instance;
	}
	
	/* isInitialized [method]: Returns true when the main stage with all its components is initialized *//**
	 * 
	 * @return
	 */
	public static boolean isInitialized() {
		return MainStageController.inizialized;
	}
	
//### PUBLIC METHODS #######################################################################################################################	
	
	public void show() {	
		this.stt.start();				//Start thread to update time!
		this.sst.start();
		this.view.show();				//Show stage!
		this.view.adjustComponents();	//Adjust components after stage is build (shown)!
	}
	
	public void setBackendIPAddress(String address) {
		this.view.lblIPAddValue.setText(address);
	}
	
	public void setBackendPort(String port) {
		this.view.lblPortValue.setText(port);
	}
	
	public void setNumberOfDevices(int devices) {
		this.view.lblDevicesValue.setText(String.valueOf(devices));
	}
	
	public void setNumberOfApps(int apps) {
		this.view.lblAppsValue.setText(String.valueOf(apps));
		
	}

	public void setNumberOfRobots(int robots) {
		this.view.lblRobotsValue.setText(String.valueOf(robots));
		
	}
	
	public void setStatus(Status status) {
		this.proStates.add(0, status);
	}
	
	public void setStatusToWaitingForDevices() {
		this.curState = Status.Waiting;
		this.view.stbStatusBar.setGraphic(this.view.imvScanning);
		this.view.sqtWaiting.play();
		this.view.stbStatusBar.setText(res.stbMsgScanning);
		
	}
	
	public void setStatusToDeviceConnected() {
		this.curState = Status.Connected;
		Media sound = new Media(res.deviceConnectedSound.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		this.view.stbStatusBar.setText(this.res.stbMsgDeviceConnected);
		this.view.stbStatusBar.setGraphic(this.view.imvConnected);
		this.view.rttConnected.play();
	}
	
	public void setStatusToDeviceDisconnected() {
		this.curState = Status.Disconnected;
		Media sound = new Media(res.deviceDisconnectedSound.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		this.view.stbStatusBar.setText(this.res.stbMsgDeviceDisconnected);
		this.view.stbStatusBar.setGraphic(this.view.imvDisconnected);
		this.view.rttDisconnected.play();
	}


	
//##########################################################################################################################################	
}
//### EOF ##################################################################################################################################