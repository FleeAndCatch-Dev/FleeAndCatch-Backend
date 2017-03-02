//### ViewController.java ##################################################################################################################

package flee_and_catch.backend.view.stage;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import flee_and_catch.backend.communication.Client;
import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.szenario.Szenario;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.controller.SzenarioController;
import flee_and_catch.backend.view.Status;
import flee_and_catch.backend.view.stage.components.CustomeRotateTransition;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

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
	private GeneralEventHandler geh;		//Handler for common / general events on the view!!
	private ActionEventHandler 	aeh;		//Handler for action events (e.g. clicks) on the view!
	private ChangeEventHandler  ceh;		//Handler for change events (e.g tree view changes) on the view!(
	private MouseEventHandler   meh;		//Handler for mouse events (e.g. mouse movement) on the view!
	private KeyEventHandler 	keh;		//Handler for key events (e.g. keypress) on view!
	private WindowEventHandler 	weh;		//Handler for window events (e.g. close over cross) on view!
	
	//Threads:
	private DetectCursorPositionThread dcpt;
	private ShowTimeThread stt;
	private ShowStatusThread sst;
	
	//Helping Variables:
	private ArrayList<Status> proStates;	//Saves the states of the program!
	private Status curState;				//Saves the status that is processed by the view!
	
//### CONSTRUCTORS #########################################################################################################################	
	
	private MainStageController() {};
	
//### INITIAL METHODS ######################################################################################################################	
	
	/* init() [method]: Initialize the main stage and all its components (must called before use)  */
	public void init() {

		//Initialize the resources for the view:
		this.res = new MainStageResources();
		//Initialize the common event handler for the view:
		this.geh = new GeneralEventHandler();
		//Initialize the action event handler for the view:
		this.aeh = new ActionEventHandler();
		//Initialize the change event handler for the view:
		this.ceh = new ChangeEventHandler();
		//Initialize the mouse event handler for the view:
		this.meh = new MouseEventHandler();
		//Initialize the key event handler for the view:
		this.keh = new KeyEventHandler();
		//Initialize the window event handler for the view:
		this.weh = new WindowEventHandler();
		//Initialize the view:
		this.view = new MainStage(res, geh, aeh, ceh, meh, keh, weh);

		this.stt = new ShowTimeThread();
		this.sst = new ShowStatusThread();
		this.dcpt = new DetectCursorPositionThread();
		
		this.proStates = new ArrayList<Status>();
		this.curState = Status.Nothing;
		
		//Set up the bindings for view components:
		this.initBindings();
		
		//Set flag that GUI is initialized:
		MainStageController.inizialized = true;

	}
	
	private void initBindings() {
		
		this.view.lblStbPackagesSync.textProperty().bind(this.res.lblStbPackagesSyncValue);
		this.view.lblStbPackagesControl.textProperty().bind(this.res.lblStbPackagesControlValue);
		this.view.lblStbPackagesScenario.textProperty().bind(this.res.lblStbPackagesScenarioValue);
		this.view.lblStbPackagesConnect.textProperty().bind(this.res.lblStbPackagesConnectValue);
		this.view.lblStbPackagesDisconnect.textProperty().bind(this.res.lblStbPackagesDisconnectValue);
		this.view.lblStbPackagesError.textProperty().bind(this.res.lblStbPackagesErrorValue);
		
		this.view.cmiSound.selectedProperty().bindBidirectional(res.soundOn);
		this.view.cmiPackagesSync.selectedProperty().bindBidirectional(res.showSyncPackages);
		this.view.cmiPackagesControl.selectedProperty().bindBidirectional(res.showControlPackages);
		this.view.cmiPackagesScenario.selectedProperty().bindBidirectional(res.showScenarioPackages);
		this.view.cmiPackagesConnect.selectedProperty().bindBidirectional(res.showConnectPackages);
		this.view.cmiPackagesDisconnect.selectedProperty().bindBidirectional(res.showDisconnectPackages);
		this.view.cmiPackagesError.selectedProperty().bindBidirectional(res.showErrorPackages);
	}
	
//### EVENT HANDLER ########################################################################################################################
	
	
	private class GeneralEventHandler implements EventHandler<Event> {

		@Override
		public void handle(Event event) {

		}
		
	}
	
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
			else if(src == MainStageController.this.view.cmiSound) {
				MainStageController.this.setCmiSoundIcon();
			}
			else if(src == MainStageController.this.view.cmiPackagesSync) {
				if(MainStageController.this.res.showSyncPackages.getValue()) {
					MainStageController.this.view.cmiPackagesSync.setGraphic(new ImageView(MainStageController.this.res.packagesScenarioIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(0, MainStageController.this.view.lblStbPackagesSync);
				}
				else {
					MainStageController.this.view.cmiPackagesSync.setGraphic(new ImageView(MainStageController.this.res.packagesScenarioOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesSync);
				}
				
			}
			else if(src == MainStageController.this.view.cmiPackagesControl) {
				if(MainStageController.this.res.showControlPackages.getValue()) {
					MainStageController.this.view.cmiPackagesControl.setGraphic(new ImageView(MainStageController.this.res.packagesControlIcon16x16));
					boolean added = false;
					int size = MainStageController.this.view.hbxStbPackagesInfo.getChildren().size();
					for(int i = 0; i < size; i++) {
						if(MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesScenario
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesConnect 
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesDisconnect
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesError) {
							MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(i, MainStageController.this.view.lblStbPackagesControl);
							added = true;
							break;
						}
					}
					if(added == false) {
						MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(size, MainStageController.this.view.lblStbPackagesControl);
					}
				}
				else {
					MainStageController.this.view.cmiPackagesControl.setGraphic(new ImageView(MainStageController.this.res.packagesControlOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesControl);
				}
			}
			else if(src == MainStageController.this.view.cmiPackagesScenario) {
				if(MainStageController.this.res.showScenarioPackages.getValue()) {
					MainStageController.this.view.cmiPackagesScenario.setGraphic(new ImageView(MainStageController.this.res.packagesScenarioIcon16x16));
					boolean added = false;
					int size = MainStageController.this.view.hbxStbPackagesInfo.getChildren().size();
					for(int i = 0; i < size; i++) {
						if(MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesConnect 
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesDisconnect
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesError) {
							MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(i, MainStageController.this.view.lblStbPackagesScenario);
							added = true;
							break;
						}
					}
					if(added == false) {
						MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(size, MainStageController.this.view.lblStbPackagesScenario);
					}
				}
				else {
					MainStageController.this.view.cmiPackagesScenario.setGraphic(new ImageView(MainStageController.this.res.packagesScenarioOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesScenario);
				}
			}
			else if(src == MainStageController.this.view.cmiPackagesConnect) {
				if(MainStageController.this.res.showConnectPackages.getValue()) {
					MainStageController.this.view.cmiPackagesConnect.setGraphic(new ImageView(MainStageController.this.res.packagesConnectIcon16x16));
					boolean added = false;
					int size = MainStageController.this.view.hbxStbPackagesInfo.getChildren().size();
					for(int i = 0; i < size; i++) {
						if(MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesDisconnect
								|| MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesError) {
							MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(i, MainStageController.this.view.lblStbPackagesConnect);
							added = true;
							break;
						}
					}
					if(added == false) {
						MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(size, MainStageController.this.view.lblStbPackagesConnect);
					}
				}
				else {
					MainStageController.this.view.cmiPackagesConnect.setGraphic(new ImageView(MainStageController.this.res.packagesConnectOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesConnect);
				}
			}
			else if(src == MainStageController.this.view.cmiPackagesDisconnect) {
				if(MainStageController.this.res.showDisconnectPackages.getValue()) {
					MainStageController.this.view.cmiPackagesDisconnect.setGraphic(new ImageView(MainStageController.this.res.packagesDisconnectIcon16x16));
					boolean added = false;
					int size = MainStageController.this.view.hbxStbPackagesInfo.getChildren().size();
					for(int i = 0; i < size; i++) {
						if(MainStageController.this.view.hbxStbPackagesInfo.getChildren().get(i) == MainStageController.this.view.lblStbPackagesError) {
							MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(i, MainStageController.this.view.lblStbPackagesDisconnect);
							added = true;
							break;
						}
					}
					if(added == false) {
						MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(size, MainStageController.this.view.lblStbPackagesDisconnect);
					}
				}
				else {
					MainStageController.this.view.cmiPackagesDisconnect.setGraphic(new ImageView(MainStageController.this.res.packagesDisconnectOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesDisconnect);
				}
			}
			else if(src == MainStageController.this.view.cmiPackagesError) {
				if(MainStageController.this.res.showErrorPackages.getValue()) {
					//Set the icon:
					MainStageController.this.view.cmiPackagesError.setGraphic(new ImageView(MainStageController.this.res.packagesErrorIcon16x16));
					int size = MainStageController.this.view.hbxStbPackagesInfo.getChildren().size();
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().add(size, MainStageController.this.view.lblStbPackagesError);
				}
				else {
					MainStageController.this.view.cmiPackagesError.setGraphic(new ImageView(MainStageController.this.res.packagesErrorOffIcon16x16));
					MainStageController.this.view.hbxStbPackagesInfo.getChildren().remove(MainStageController.this.view.lblStbPackagesError);
				}
			}
			
		}
		
	}
	
	/* ChangeEventHandler [class]: Class that implements an change listener that handles all component changes of the view *//**
	 * 
	 * @author mbothner
	 *
	 */
	private class ChangeEventHandler implements ChangeListener<Object> {

		@Override
		public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
			
			TreeItem<String> curItem = MainStageController.this.view.trvDeviceTree.getSelectionModel().getSelectedItem();
			
			if(Pattern.matches(res.triAppText + "[0-9]+", curItem.getValue())) {
				MainStageController.this.showAppInfo(Integer.parseInt(curItem.getValue().substring(8)));
			}
			else if(Pattern.matches(res.triRobotText+ "[0-9]+", curItem.getValue())) {
				MainStageController.this.showRobotInfo(Integer.parseInt(curItem.getValue().substring(10)));
			}
			else if(Pattern.matches(res.triScenariosText + "[0-9]+", curItem.getValue())) {
				MainStageController.this.showScenarioInfo(Integer.parseInt(curItem.getValue().substring(13)));
			}
			else {
				MainStageController.this.clearInfo();
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
			
			if(event.getTarget() == MainStageController.this.view.gdpMainPane) {
				//Close all menus when cursor leaves the menu bar:
				for(Menu menu : MainStageController.this.view.mnbMenuBar.getMenus()) {
					menu.hide();
				}
			}
			else if(event.getSource() == MainStageController.this.view.mnbMenuBar) {
				MainStageController.this.view.mnbMenuBar.requestFocus();
			}
			else {
				//System.out.println("Move CurPosX: " + event.getSceneX() + " CurPoxY: " + event.getSceneY());
				
			}

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
	private class DetectCursorPositionThread extends Thread {
		
		@Override
		public void run() {
			
			//Run forever:
			while(true) {
				
				//
				Platform.runLater(new Runnable() { @Override public void run() {
					
					//Get cursor position on screen:
					Point p = MouseInfo.getPointerInfo().getLocation();
					//Determine where the scene begins on the screen:
					int sceneX =  (int) (MainStageController.this.view.getX() + MainStageController.this.view.scene.getX());
					int sceneY =  (int) (MainStageController.this.view.getY() + MainStageController.this.view.scene.getY());
					//The meu bar begins on the first pixel of the scene:
					int menuBarStartX = sceneX;
					int menuBarStartY = sceneY;
					
					int menuBarEndX = (int) (menuBarStartX + MainStageController.this.view.scene.getWidth());
					int menuBarEndY = (int) (menuBarStartY + MainStageController.this.view.mnbMenuBar.getHeight());
					
					//Check if cursor is in the menu bar:
					if(p.x >= menuBarStartX && p.x <= menuBarEndX && p.y >= menuBarStartY && p.y <= menuBarEndY) {
						//System.out.println("CURSOR IS IN THE MENUBAR!!!");
						int menuStartX = menuBarStartX + 9; 	//First element begins 9 pixel after menu bar start!
						int menuEndX = 0;
						int menuStartY = menuBarStartY;
						int menuEndY   = menuBarEndY;
						for(Menu menu : MainStageController.this.view.mnbMenuBar.getMenus()) {
							
							String menuId = menu.getId();
							
							Label lblPseudoMenu = (Label) MainStageController.this.view.scene.lookup("#"+ menuId + "Pseudo");

							menuEndX = (int) (menuStartX + lblPseudoMenu.getWidth() + 18);
							CustomeRotateTransition node = (CustomeRotateTransition) MainStageController.this.view.scene.lookup("#" + menuId + "Rtt");
							if(p.x >= menuStartX && p.x <= menuEndX && p.y >= menuStartY && p.y <= menuEndY) {
								if(node.getRotateTransition().getStatus() == Animation.Status.STOPPED) {
									node.getRotateTransition().playFromStart();
								}
							}
							else {
						        //Reset the clock-image to the start-position:
						        node.getRotateTransition().jumpTo(Duration.ZERO);
								node.getRotateTransition().stop();
							}
							menuStartX = menuEndX + 1;
							
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
					MainStageController.this.view.lblStbTime.setText(time);
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
	
	/* ShowStatusThread [class]: Thread that updates the status of the status bar of the stage *//**
	 * 
	 * @author mbothner
	 *
	 */
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
								MainStageController.this.processStatusToDeviceConnected();
								break;
							case Disconnected:
								MainStageController.this.processStatusToDeviceDisconnected();
								break;
							case Waiting:
								MainStageController.this.processStatusToWaitingForDevices();
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
	
	private void setCmiSoundIcon() {
		if(this.res.soundOn.get()) {
			this.view.cmiSound.setGraphic(new ImageView(this.res.soundIcon16x16));
		}
		else {
			this.view.cmiSound.setGraphic(new ImageView(this.res.soundMuteIcon16x16));
		}
	}
	
	private void processStatusToWaitingForDevices() {
		this.curState = Status.Waiting;
		this.view.stbStatusBar.setGraphic(this.view.imvScanning);
		this.view.sqtWaiting.play();
		this.view.stbStatusBar.setText(res.stbMsgScanning);
		
	}
	
	private void processStatusToDeviceConnected() {
		this.curState = Status.Connected;
		if(this.res.soundOn.getValue()) {
			Media sound = new Media(res.deviceConnectedSound.toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		}
		this.view.stbStatusBar.setText(this.res.stbMsgDeviceConnected);
		this.view.stbStatusBar.setGraphic(this.view.imvConnected);
		this.view.rttConnected.play();
	}
	
	private void processStatusToDeviceDisconnected() {
		this.curState = Status.Disconnected;
		if(this.res.soundOn.getValue()) {
			Media sound = new Media(res.deviceDisconnectedSound.toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		}
		this.view.stbStatusBar.setText(this.res.stbMsgDeviceDisconnected);
		this.view.stbStatusBar.setGraphic(this.view.imvDisconnected);
		this.view.rttDisconnected.play();
	}
	
	private void showAppInfo(int deviceID) {
		
		String infoText = "Device Info:\n";
		
		for(App app : AppController.getApps()) {
			if(app.getIdentification().getId() == deviceID) {
				infoText += "ID: " + app.getIdentification().getId() + "\n";
				infoText += "Type: " + app.getIdentification().getType() + "\n";
				infoText += "Roletype: " + app.getIdentification().getRoletype() + "\n";
			}
		}
		for(Client client : Server.getClients()) {
			if(client.getIdentification().getId() == deviceID) {
				infoText += "Active: " + client.getDevice().isActive() + "\n";
				//infoText += "Address: " + client.getIdentification().getAddress() + "\n";
				int index = client.getSocket().getRemoteSocketAddress().toString().indexOf(":");
				infoText += "Address: " + client.getSocket().getRemoteSocketAddress().toString().substring(1, index) + "\n";
				//infoText += "Port: " + client.getIdentification().getPort() + "\n";
				infoText += "Port: " + client.getSocket().getPort() + "\n";
				if(client.getSzenario() == null) {
					infoText += "Scenario-ID: No Scenario\n";
					infoText += "Scenario-Type: No Scenario\n";
				}
				else {
					infoText += "Scenario-Type: " + client.getSzenario().getType() + "\n";
					infoText += "Scenario-Command: " + client.getSzenario().getCommand() + "\n";
				}
			}
		}
		this.view.txaDeviceInfo.setText(infoText);
	}
	
	private void showRobotInfo(int deviceID) {
		
		String infoText = "Device Info:\n";
		
		for(Robot robot : RobotController.getRobots()) {
			if(robot.getIdentification().getId() == deviceID) {
				infoText += "ID: " + robot.getIdentification().getId() + "\n";
				infoText += "Type: " + robot.getIdentification().getType() + "\n";
				infoText += "Subtype: " + robot.getIdentification().getSubtype() + "\n";
				infoText += "Role: " + robot.getIdentification().getRole() + "\n";
			}
		}
		for(Client client : Server.getClients()) {
			if(client.getIdentification().getId() == deviceID) {
				infoText += "Active: " + client.getDevice().isActive() + "\n";
				//infoText += "Address: " + client.getIdentification().getAddress() + "\n";
				int index = client.getSocket().getRemoteSocketAddress().toString().indexOf(":");
				infoText += "Address: " + client.getSocket().getRemoteSocketAddress().toString().substring(1, index) + "\n";
				//infoText += "Port: " + client.getIdentification().getPort() + "\n";
				infoText += "Port: " + client.getSocket().getPort() + "\n";
				if(client.getSzenario() == null) {
					infoText += "Scenario-ID: No Scenario\n";
					infoText += "Scenario-Type: No Scenario\n";
				}
				else {
					infoText += "Scenario-Type: " + client.getSzenario().getType() + "\n";
					infoText += "Scenario-Command: " + client.getSzenario().getCommand() + "\n";
				}
			}
		}
		this.view.txaDeviceInfo.setText(infoText);
	}
	
	private void showScenarioInfo(int scenarioID) {
		
	}
	
	private void clearInfo() {
		this.view.txaDeviceInfo.setText("");
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
		this.dcpt.start();
	}
	
	//### Methods to set general info ##########################################
	
	public void setBackendIPAddress(String address) {
		this.view.lblIPAddValue.setText(address);
	}
	
	public void setBackendPort(String port) {
		this.view.lblPortValue.setText(port);
	}
	
	public void setNumberOfDevices(int devices) {
		this.view.lblDevicesValue.setText(String.valueOf(devices));
		this.view.triDevices.setValue(this.res.triDevicesText + " (" + devices + ")");
		this.view.triAll.setValue(this.res.triDevicesText + " (" + devices + ")");
	}
	
	public void setNumberOfApps(int apps) {
		this.view.lblAppsValue.setText(String.valueOf(apps));
		this.view.triAllApps.setValue(this.res.triAllAppsText + " (" + apps + ")");
		
		List<App> applist = (ArrayList<App>) AppController.getApps();
		
		this.view.triAllApps.getChildren().clear();
			
		for(App app : applist) {
			TreeItem<String> triApp = new TreeItem<String>(res.triAppText + app.getIdentification().getId());
			this.view.triAllApps.getChildren().add(triApp);
		}
		
	}

	public void setNumberOfRobots(int robots) {
		this.view.lblRobotsValue.setText(String.valueOf(robots));
		this.view.triAllRobots.setValue(this.res.triAllRobotsText + " (" + robots + ")");
		
		List<Robot> robotlist = (ArrayList<Robot>) RobotController.getRobots();
		
		this.view.triAllRobots.getChildren().clear();
			
		for(Robot robot : robotlist) {
			TreeItem<String> triRobot = new TreeItem<String>(res.triRobotText + robot.getIdentification().getId());
			triRobot.addEventHandler(ActionEvent.ACTION, this.aeh);
			this.view.triAllRobots.getChildren().add(triRobot);
		}
	}
	
	public void setNumberOfScenarios(int scenarios) {
		
		this.view.lblScenariosValue.setText(String.valueOf(scenarios));
		this.view.triScenarios.setValue(this.res.triScenariosText + " (" + scenarios + ")");
		
		List<Szenario> scenarioList = (ArrayList<Szenario>) SzenarioController.getSzenarios();
		
		this.view.triScenarios.getChildren().clear();
			
		for(Szenario scenario : scenarioList) {
			TreeItem<String> triScenario = new TreeItem<String>(res.triScenarioText + scenario.getId());
			triScenario.addEventHandler(ActionEvent.ACTION, this.aeh);
			this.view.triAllRobots.getChildren().add(triScenario);
		}
	}
	
	//### Methods for status line ##############################################
	
	public void setStatus(Status status) {
		this.proStates.add(0, status);
	}

	//### Methods for packet counters ##########################################
	
	public void increaseNoOfSyncPackages() {
		int noOfSP = Integer.parseInt(this.res.lblStbPackagesSyncValue.getValue());
		noOfSP++;
		this.res.lblStbPackagesSyncValue.setValue(String.valueOf(noOfSP));
	}
	
	public void increaseNoOfControlPackages() {
		int noOfCP = Integer.parseInt(this.res.lblStbPackagesControlValue.getValue());
		noOfCP++;
		this.res.lblStbPackagesControlValue.setValue(String.valueOf(noOfCP));
	}
	
	public void increaseNoOfScenarioPackages() {
		int noOfSP = Integer.parseInt(this.res.lblStbPackagesScenarioValue.getValue());
		noOfSP++;
		this.res.lblStbPackagesScenarioValue.setValue(String.valueOf(noOfSP));
	}
	
	public void increaseNoOfConnectPackages() {
		int noOfCP = Integer.parseInt(this.res.lblStbPackagesConnectValue.getValue());
		noOfCP++;
		this.res.lblStbPackagesConnectValue.setValue(String.valueOf(noOfCP));
	}
	
	public void increaseNoOfDisconnectPackages() {
		int noOfDP = Integer.parseInt(this.res.lblStbPackagesDisconnectValue.getValue());
		noOfDP++;
		this.res.lblStbPackagesDisconnectValue.setValue(String.valueOf(noOfDP));
	}
	
	//### Methods to update sensor and control data ############################
	
	public void updateSensorData(int deviceID) {
		
		TreeItem<String> curItem = MainStageController.this.view.trvDeviceTree.getSelectionModel().getSelectedItem();
		
		//If a robot is selected in the list:
		if(Pattern.matches(res.triRobotText + "[0-9]+", curItem.getValue())) {
			//If the right number (robot-id) is selected in the list:
			if(deviceID == Integer.parseInt(curItem.getValue().substring(10))) {
				
			}
		}

	}
	
	public void updateControlData(int deviceID) {
		
		TreeItem<String> curItem = MainStageController.this.view.trvDeviceTree.getSelectionModel().getSelectedItem();
		
		if(Pattern.matches(res.triAppText + "[0-9]+", curItem.getValue())) {
			//If the right number (robot-id) is selected in the list:
			if(deviceID == Integer.parseInt(curItem.getValue().substring(10))) {
				
			}
		}
		
	}
	
	public void printDebugLine(String string) {
		this.view.txaDebugInfo.setText(this.view.txaDebugInfo.getText() + "\n" + string);
	}
	
	
//##########################################################################################################################################	
}
//### EOF ##################################################################################################################################