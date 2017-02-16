//### MainStage.java #######################################################################################################################

package flee_and_catch.backend.view.stage;

//### IMPORTS ##############################################################################################################################
import org.controlsfx.control.StatusBar;

import flee_and_catch.backend.view.stage.components.CustomeRotateTransition;
import flee_and_catch.backend.view.stage.components.TitledBorderPane;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainStage extends Stage {
	
//### COMPONENTS ###########################################################################################################################

	//Scene of the stage:
	Scene scene;

	//Root pane (container) that contains all GUI components:
	StackPane skpRootPane;
	BorderPane bdpRootPane;
	GridPane gdpPseudoPane;
	//Menu bar at the top of the window:
	MenuBar mnbMenuBar;
	
	ImageView imvProgram;
	Menu mnuProgram;
	Label lblPseudoMnuProgram;
	CustomeRotateTransition rttMnuProgram;
	
	ImageView imvSettings;
	Menu mnuSettings;
	Label lblPseudoMnuSettings;
	CustomeRotateTransition rttMnuSettings;
	CheckMenuItem cmiSound;
	SeparatorMenuItem smiMnuSettings;
	CheckMenuItem cmiPackagesSync;
	CheckMenuItem cmiPackagesControl;
	CheckMenuItem cmiPackagesScenario;
	CheckMenuItem cmiPackagesConnect;
	CheckMenuItem cmiPackagesDisconnect;
	CheckMenuItem cmiPackagesError;
	MenuItem mniExit;
	//Status bar for the bottom of the window:
	StatusBar stbStatusBar;
	ImageView imvScanning;
	ImageView imvConnected;
	ImageView imvDisconnected;
	SequentialTransition sqtWaiting;
	RotateTransition rttConnected;
	RotateTransition rttDisconnected;
	HBox      hbxStbPackagesInfo;
	Label     lblStbPackagesSync;
	Label     lblStbPackagesControl;
	Label     lblStbPackagesScenario;
	Label     lblStbPackagesConnect;
	Label     lblStbPackagesDisconnect;
	Label     lblStbPackagesError;
	Label     lblStbTime;
	//Main pane for the content of the view:
	GridPane gdpMainPane;
	
	//### Content ##############################################################
	
	TitledBorderPane tbpBackendInfo;
	GridPane gdpBackendInfo;
	Label lblIPAddText;
	Label lblIPAddValue;
	Label lblPortText;
	Label lblPortValue;
	Label lblDevicesText;
	Label lblDevicesValue;
	Label lblRobotsText;
	Label lblRobotsValue;
	Label lblAppsText;
	Label lblAppsValue;
	Label lblScenariosText;
	Label lblScenariosValue;
	
	TitledBorderPane tbpDeviceInfo;
	GridPane gdpDeviceInfo;
	TextArea txaDeviceInfo;
	ScrollPane scpDeviceTreePane;
	TreeView<String> trvDeviceTree;
	TreeItem<String> triDevices;
	TreeItem<String> triAll;
	TreeItem<String> triAllRobots;
	TreeItem<String> triAllApps;
	TreeItem<String> triScenarios;
	
//### CONSTRUCTORS #########################################################################################################################

	public MainStage(
			MainStageResources res, 
			EventHandler<Event>			geh,
			EventHandler<ActionEvent>	aeh,
			ChangeListener<Object>      ceh,
			EventHandler<MouseEvent>  	meh,
			EventHandler<KeyEvent>    	keh, 
			EventHandler<WindowEvent> 	weh) {
		
		this.initComponents(res, geh, aeh, ceh, meh, keh);
		this.structureComponents();
		this.initStage(res, meh, weh);
	}
	
//### INITAL METHODS #######################################################################################################################
	
	private void initComponents(		
			MainStageResources res, 
			EventHandler<Event>			geh,
			EventHandler<ActionEvent>	aeh,
			ChangeListener<Object>      ceh,
			EventHandler<MouseEvent>  	meh,
			EventHandler<KeyEvent>    	keh) {
		
		
		//### Basic layout elements ################################################################
		
		this.skpRootPane = new StackPane();
		this.bdpRootPane = new BorderPane();
		this.gdpPseudoPane = new GridPane();
		
		this.mnbMenuBar = new MenuBar();
		this.mnbMenuBar.setUseSystemMenuBar(false);
		this.mnbMenuBar.setFocusTraversable(true);
		this.mnbMenuBar.addEventHandler(MouseEvent.MOUSE_CLICKED, meh);
		
		this.imvProgram = new ImageView(res.programIcon16x16);
		this.imvProgram.setId("imvProgram");
		this.mnuProgram = new Menu(res.mnuProgramText);
		this.mnuProgram.setId("mnuProgram");
		this.mnuProgram.setGraphic(this.imvProgram);
		this.lblPseudoMnuProgram = new Label(res.mnuProgramText);
		this.lblPseudoMnuProgram.setId("mnuProgramPseudo");
		this.lblPseudoMnuProgram.setGraphic(this.imvProgram);
		this.rttMnuProgram = new CustomeRotateTransition();
		this.rttMnuProgram.setId("mnuProgramRtt");
		this.rttMnuProgram.setRotateTransition(new RotateTransition(Duration.millis(2000), this.imvProgram));
		this.rttMnuProgram.getRotateTransition().setByAngle(360);
		this.rttMnuProgram.getRotateTransition().setCycleCount(1);
		this.rttMnuProgram.getRotateTransition().setAxis(Rotate.Y_AXIS);
		
		this.mniExit    = new MenuItem(res.mniExitText);
		this.mniExit.setGraphic(new ImageView(res.exitIcon16x16));
		this.mniExit.setOnAction(aeh);
		this.mniExit.setId("mniExit");
		
		this.imvSettings = new ImageView(res.settingsIcon16x16);
		this.imvSettings.setId("imvSettings");
		this.mnuSettings = new Menu(res.mnuSettingsText);
		this.mnuSettings.setId("mnuSettings");
		this.mnuSettings.setGraphic(this.imvSettings);
		this.lblPseudoMnuSettings = new Label(res.mnuSettingsText);
		this.lblPseudoMnuSettings.setId("mnuSettingsPseudo");
		this.lblPseudoMnuSettings.setGraphic(this.imvSettings);
		this.rttMnuSettings = new CustomeRotateTransition();
		this.rttMnuSettings.setId("mnuSettingsRtt");
		this.rttMnuSettings.setRotateTransition(new RotateTransition(Duration.millis(2000), this.imvSettings));
		this.rttMnuSettings.getRotateTransition().setByAngle(360);
		this.rttMnuSettings.getRotateTransition().setCycleCount(1);
		this.rttMnuSettings.getRotateTransition().setAxis(Rotate.Y_AXIS);

		this.cmiSound = new CheckMenuItem(res.cmiSoundText);
		this.cmiSound.setGraphic(new ImageView(res.soundIcon16x16));
		this.cmiSound.setOnAction(aeh);
		this.smiMnuSettings = new SeparatorMenuItem();
		this.cmiPackagesSync = new CheckMenuItem(res.cmiPackagesSyncText);
		this.cmiPackagesSync.setGraphic(new ImageView(res.packagesSyncIcon16x16));
		this.cmiPackagesSync.setOnAction(aeh);
		this.cmiPackagesControl = new CheckMenuItem(res.cmiPackagesControlText);
		this.cmiPackagesControl.setGraphic(new ImageView(res.packagesControlIcon16x16));
		this.cmiPackagesControl.setOnAction(aeh);
		this.cmiPackagesScenario = new CheckMenuItem(res.cmiPackagesScenarioText);
		this.cmiPackagesScenario.setGraphic(new ImageView(res.packagesScenarioIcon16x16));
		this.cmiPackagesScenario.setOnAction(aeh);
		this.cmiPackagesConnect = new CheckMenuItem(res.cmiPackagesConnectText);
		this.cmiPackagesConnect.setGraphic(new ImageView(res.packagesConnectIcon16x16));
		this.cmiPackagesConnect.setOnAction(aeh);
		this.cmiPackagesDisconnect = new CheckMenuItem(res.cmiPackagesDisconnectText);
		this.cmiPackagesDisconnect.setGraphic(new ImageView(res.packagesDisconnectIcon16x16));
		this.cmiPackagesDisconnect.setOnAction(aeh);
		this.cmiPackagesError = new CheckMenuItem(res.cmiPackagesErrorText);
		this.cmiPackagesError.setGraphic(new ImageView(res.packagesErrorIcon16x16));
		this.cmiPackagesError.setOnAction(aeh);
		
		this.stbStatusBar = new StatusBar();
		this.stbStatusBar.setText("");
		this.imvScanning = new ImageView(res.scanningIcon16x16);
		this.imvConnected = new ImageView(res.connectedIcon16x16);
		this.imvDisconnected = new ImageView(res.disconnectedIcon16x16);
		RotateTransition rotScanning = new RotateTransition(Duration.millis(2000), this.imvScanning);
		rotScanning.setByAngle(360);
		this.sqtWaiting = new SequentialTransition(new PauseTransition(Duration.millis(600)), rotScanning);
		this.sqtWaiting.setCycleCount(Transition.INDEFINITE);
		this.sqtWaiting.setDelay(Duration.millis(600));
		
		this.rttConnected = new RotateTransition(Duration.millis(1600), this.imvConnected);
		this.rttConnected.setByAngle(360);
		this.rttConnected.setCycleCount(1);
		this.rttConnected.setDelay(Duration.millis(100));
		this.rttConnected.setAxis(Rotate.Y_AXIS);
		this.rttConnected.setOnFinished(aeh);
		
		this.rttDisconnected = new RotateTransition(Duration.millis(1600), this.imvDisconnected);
		this.rttDisconnected.setByAngle(360);
		this.rttDisconnected.setCycleCount(1);
		this.rttDisconnected.setDelay(Duration.millis(100));
		this.rttDisconnected.setAxis(Rotate.Y_AXIS);
		this.rttDisconnected.setOnFinished(aeh);
		
		this.lblStbTime = new Label();
		this.lblStbTime.setGraphic(new ImageView(res.clockIcon16x16));
		this.hbxStbPackagesInfo = new HBox(15);
		this.hbxStbPackagesInfo.setPadding(new Insets(0,20,0,20));
		
		this.lblStbPackagesConnect = new Label();
		this.lblStbPackagesConnect.setGraphic(new ImageView(res.packagesConnectIcon16x16));
		this.lblStbPackagesDisconnect = new Label();
		this.lblStbPackagesDisconnect.setGraphic(new ImageView(res.packagesDisconnectIcon16x16));
		this.lblStbPackagesSync = new Label();
		this.lblStbPackagesSync.setGraphic(new ImageView(res.packagesSyncIcon16x16));
		this.lblStbPackagesControl = new Label();
		this.lblStbPackagesControl.setGraphic(new ImageView(res.packagesControlIcon16x16));
		this.lblStbPackagesScenario = new Label();
		this.lblStbPackagesScenario.setGraphic(new ImageView(res.packagesScenarioIcon16x16));
		this.lblStbPackagesError = new Label();
		this.lblStbPackagesError.setGraphic(new ImageView(res.packagesErrorIcon16x16));
		
		this.gdpMainPane = new GridPane();
		this.gdpMainPane.setPadding(new Insets(20,15,15,15));
		this.gdpMainPane.setVgap(20);
		this.gdpMainPane.setOnMouseEntered(meh);

		//### Backend information ##################################################################
		
		this.tbpBackendInfo    = new TitledBorderPane(res.tbpBackendInfoText);
		this.tbpBackendInfo.setPrefWidth(10000);
		this.tbpBackendInfo.setIcon(res.backendIcon16x16);
		
		this.gdpBackendInfo    = new GridPane();
		this.gdpBackendInfo.setPrefWidth(10000);
		this.gdpBackendInfo.setHgap(10);
		this.gdpBackendInfo.setVgap(10);
		ColumnConstraints colBI1 = new ColumnConstraints();
		colBI1.setHgrow(Priority.SOMETIMES);
		ColumnConstraints colBI2 = new ColumnConstraints();
		colBI2.setHgrow(Priority.ALWAYS);
		this.gdpBackendInfo.getColumnConstraints().addAll(colBI1, colBI2, colBI1, colBI2, colBI1, colBI2);
		
		this.lblIPAddText      = new Label(res.lblIPAddText);
		this.lblIPAddText.setGraphic(new ImageView(res.ipaddIcon16x16));
		this.lblIPAddValue     = new Label();
		this.lblPortText       = new Label(res.lblPortText);
		this.lblPortText.setGraphic(new ImageView(res.portIcon16x16));
		this.lblPortValue      = new Label();
		this.lblDevicesText    = new Label(res.lblDevicesText);
		this.lblDevicesText.setGraphic(new ImageView(res.deviceIcon16x16));
		this.lblDevicesValue   = new Label("0");
		this.lblRobotsText     = new Label(res.lblRobotsText);
		this.lblRobotsText.setGraphic(new ImageView(res.robotIcon16x16));
		this.lblRobotsValue    = new Label("0");
		this.lblAppsText       = new Label(res.lblAppsText);
		this.lblAppsText.setGraphic(new ImageView(res.appIcon16x16));
		this.lblAppsValue      = new Label("0");
		this.lblScenariosText  = new Label(res.lblScenariosText);
		this.lblScenariosText.setGraphic(new ImageView(res.scenariosIcon16x16));
		this.lblScenariosValue = new Label("0");
		
		//### Device information ###################################################################
		
		this.tbpDeviceInfo = new TitledBorderPane(res.tbpDevicesInfoText);
		this.tbpDeviceInfo.setPrefWidth(10000);
		this.tbpDeviceInfo.setPrefHeight(10000);
		this.tbpDeviceInfo.setIcon(res.deviceIcon16x16);
		
		this.gdpDeviceInfo = new GridPane();
		this.gdpDeviceInfo.setPrefWidth(10000);
		this.gdpDeviceInfo.setPrefHeight(10000);
		this.gdpDeviceInfo.setHgap(20);
		ColumnConstraints colDI1 = new ColumnConstraints();
		colDI1.setHgrow(Priority.ALWAYS);
		ColumnConstraints colDI2 = new ColumnConstraints();
		colDI2.setHgrow(Priority.NEVER);
		colDI2.setHalignment(HPos.RIGHT);
		this.gdpBackendInfo.getColumnConstraints().addAll(colDI1, colDI2);
		
		this.txaDeviceInfo = new TextArea();
		this.txaDeviceInfo.setId("txaDeviceInfo");
		this.txaDeviceInfo.setPrefWidth(10000);
		this.txaDeviceInfo.setPrefHeight(10000);
		this.txaDeviceInfo.setEditable(false);
		this.scpDeviceTreePane = new ScrollPane();
		this.scpDeviceTreePane.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scpDeviceTreePane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.scpDeviceTreePane.setPrefHeight(10000);
		this.scpDeviceTreePane.setMinWidth(200);
		this.scpDeviceTreePane.setPrefWidth(200);
		
		this.trvDeviceTree = new TreeView<String>();
		this.trvDeviceTree.setPrefHeight(10000);
		this.trvDeviceTree.setPrefWidth(200);
		this.trvDeviceTree.getSelectionModel().selectedItemProperty().addListener(ceh);
		this.triDevices = new TreeItem<String>(res.triDevicesText + " (0)");
		this.triDevices.setGraphic(new ImageView(res.deviceIcon16x16));
		this.triAll = new TreeItem<String>(res.triAllText + " (0)");
		this.triAll.setGraphic(new ImageView(res.allIcon16x16));
		this.triAllRobots = new TreeItem<String>(res.triAllRobotsText + " (0)");
		this.triAllRobots.setGraphic(new ImageView(res.robotIcon16x16));
		this.triAllApps = new TreeItem<String>(res.triAllAppsText + " (0)");
		this.triAllApps.setGraphic(new ImageView(res.appIcon16x16));
		this.triScenarios = new TreeItem<String>(res.triScenariosText + " (0)");
		this.triScenarios.setGraphic(new ImageView(res.scenariosIcon16x16));
	}
	
	private void structureComponents() {
		
		//### Backend information ##################################################################
		
		//Fill grid pane with labels:
		this.gdpBackendInfo.add(this.lblIPAddText, 0, 0);
		this.gdpBackendInfo.add(this.lblIPAddValue, 1, 0);
		this.gdpBackendInfo.add(this.lblPortText, 2, 0);
		this.gdpBackendInfo.add(this.lblPortValue, 3, 0);
		this.gdpBackendInfo.add(this.lblDevicesText, 0, 1);
		this.gdpBackendInfo.add(this.lblDevicesValue, 1, 1);
		this.gdpBackendInfo.add(this.lblRobotsText, 2, 1);
		this.gdpBackendInfo.add(this.lblRobotsValue, 3, 1);
		this.gdpBackendInfo.add(this.lblAppsText, 4, 1);
		this.gdpBackendInfo.add(this.lblAppsValue, 5, 1);
		this.gdpBackendInfo.add(this.lblScenariosText, 0, 2);
		this.gdpBackendInfo.add(this.lblScenariosValue, 1, 2, 5, 1);
		//Add grid pane to titled border pane:
		this.tbpBackendInfo.setContent(this.gdpBackendInfo);
		
		//### Device information ###################################################################
		
		//Setuo tree view:
		this.triAll.getChildren().add(this.triAllRobots);
		this.triAll.getChildren().add(this.triAllApps);
		this.triDevices.getChildren().add(this.triAll);
		this.triDevices.getChildren().add(this.triScenarios);
		this.trvDeviceTree.setRoot(this.triDevices);
		//Add tree view to scroll pane:
		this.scpDeviceTreePane.setContent(this.trvDeviceTree);
		//Add text field and scroll pane to grid pane:
		this.gdpDeviceInfo.add(this.txaDeviceInfo, 0, 0);
		this.gdpDeviceInfo.add(this.scpDeviceTreePane, 1, 0);
		//Add grid pane to titled border pane:
		this.tbpDeviceInfo.setContent(this.gdpDeviceInfo);
		
		//### Main panes ###########################################################################
		
		//Add grid panes to main grid pane
		this.gdpMainPane.add(this.tbpBackendInfo, 0, 0);
		this.gdpMainPane.add(this.tbpDeviceInfo, 0, 1);
		this.gdpMainPane.autosize();
		
		//Setuo menu bar:
		this.mnuProgram.getItems().add(this.mniExit);
		this.mnuSettings.getItems().addAll(
				this.cmiSound, 
				this.smiMnuSettings, 
				this.cmiPackagesSync,
				this.cmiPackagesControl,
				this.cmiPackagesScenario,
				this.cmiPackagesConnect,
				this.cmiPackagesDisconnect,
				this.cmiPackagesError);
		this.mnbMenuBar.getMenus().addAll(this.mnuProgram, this.mnuSettings);
		
		//Setup status bar:
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesSync);
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesControl);
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesScenario);
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesConnect);
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesDisconnect);
		this.hbxStbPackagesInfo.getChildren().add(this.lblStbPackagesError);
		this.stbStatusBar.getRightItems().add(this.hbxStbPackagesInfo);
		this.stbStatusBar.getRightItems().add(this.lblStbTime);
		//Add main pane / menu bar / status bar to root pane:
		this.bdpRootPane.setTop(this.mnbMenuBar);
		this.bdpRootPane.setBottom(this.stbStatusBar);
		this.bdpRootPane.setCenter(this.gdpMainPane);
		
		this.gdpPseudoPane.getChildren().add(this.lblPseudoMnuProgram);
		this.gdpPseudoPane.getChildren().add(this.rttMnuProgram);
		this.gdpPseudoPane.getChildren().add(this.lblPseudoMnuSettings);
		this.gdpPseudoPane.getChildren().add(this.rttMnuSettings);
		
		this.skpRootPane.getChildren().addAll(this.gdpPseudoPane, this.bdpRootPane);
		
	}
	
	private void initStage(MainStageResources res, EventHandler<MouseEvent> meh, EventHandler<WindowEvent> weh) {
		
		//Create scene and set their properties:
		this.scene = new Scene(this.skpRootPane, 700, 500);
		this.scene.getStylesheets().add(MainStage.class.getResource("MainStageCSS.css").toExternalForm());
		this.addEventHandler(MouseEvent.ANY, meh);
		//Set stage properties:
		this.setScene(this.scene);
		this.setTitle(res.stageTitle);
		this.getIcons().addAll(
				res.fleeAndCatchLogo16x16, 
				res.fleeAndCatchLogo24x24,
				res.fleeAndCatchLogo32x32,
				res.fleeAndCatchLogo48x48,
				res.fleeAndCatchLogo64x64,
				res.fleeAndCatchLogo128x128,
				res.fleeAndCatchLogo256x256);
		this.setMinWidth(550);
		this.setMinHeight(420);
		this.setOnCloseRequest(weh);
		this.scpDeviceTreePane.requestFocus();
	}
	
	public void adjustComponents() {
		this.trvDeviceTree.autosize();
		this.tbpBackendInfo.adjustTitleSize();
		this.tbpDeviceInfo.adjustTitleSize();
		this.lblStbTime.setTranslateY(4.0);	//Needed because status bar set label not in the middle!
		this.lblStbPackagesConnect.setTranslateY(4.0);	//Needed because status bar set label not in the middle!
		this.lblStbPackagesDisconnect.setTranslateY(4.0);
		this.lblStbPackagesSync.setTranslateY(4.0);
		this.lblStbPackagesControl.setTranslateY(4.0);
		this.lblStbPackagesScenario.setTranslateY(4.0);
		this.lblStbPackagesError.setTranslateY(4.0);
	}
	
	
//##########################################################################################################################################
}
//### EOF ##################################################################################################################################