//### MainStageResources.java ##############################################################################################################

package flee_and_catch.backend.view.stage;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class MainStageResources {
	
//### ATTRIBUTES ###########################################################################################################################
	
	//### Texts ####################################################################################	
	
	//Stage title:
	String stageTitle;
	//Label texts:
	String lblIPAddText;
	String lblPortText;
	String lblDevicesText;
	String lblRobotsText;
	String lblAppsText;
	String lblScenariosText;
	//Titled border pane titles:
	String tbpBackendInfoText;
	String tbpDevicesInfoText;
	//Texts of the tree view item:
	String triDevicesText;
	String triAllText;
	String triAllRobotsText;
	String triAllAppsText;
	String triScenariosText;
	String triAppText;
	String triRobotText;
	String triScenarioText;
	//Menu bar texts:
	String mnuProgramText;
	String mniExitText;
	String mnuSettingsText;
	String cmiSoundText;
	String cmiPackagesSyncText;
	String cmiPackagesControlText;
	String cmiPackagesScenarioText;
	String cmiPackagesConnectText;
	String cmiPackagesDisconnectText;
	String cmiPackagesErrorText;
	//Status bar message:
	String stbMsgScanning;
	String stbMsgDeviceConnected;
	String stbMsgDeviceDisconnected;
	StringProperty lblStbPackagesConnectValue;
	StringProperty lblStbPackagesDisconnectValue;
	StringProperty lblStbPackagesSyncValue;
	StringProperty lblStbPackagesControlValue;
	StringProperty lblStbPackagesScenarioValue;
	StringProperty lblStbPackagesErrorValue;
	//Dialog texts:
	String cpdTitle;
	String cpdHeaderText;
	String cpdContentText;
	String cpdBtnApplyText;
	String cpdBtnCancelText;
	
	//### Images ###################################################################################
	
	//Flee and catch logo:
	Image fleeAndCatchLogo16x16;
	Image fleeAndCatchLogo24x24;
	Image fleeAndCatchLogo32x32;
	Image fleeAndCatchLogo48x48;
	Image fleeAndCatchLogo64x64;
	Image fleeAndCatchLogo128x128;
	Image fleeAndCatchLogo256x256;
	//Menu bar icons:
	Image programIcon16x16;
	Image exitIcon16x16;
	Image settingsIcon16x16;
	Image soundIcon16x16;
	Image soundMuteIcon16x16;
	Image packagesConnectOffIcon16x16;
	Image packagesDisconnectOffIcon16x16;
	Image packagesSyncOffIcon16x16;
	Image packagesControlOffIcon16x16;
	Image packagesScenarioOffIcon16x16;
	Image packagesErrorOffIcon16x16;
	//Content icons:
	Image deviceIcon16x16;
	Image allIcon16x16;
	Image robotIcon16x16;
	Image appIcon16x16;
	Image scenariosIcon16x16;
	Image ipaddIcon16x16;
	Image portIcon16x16;
	Image backendIcon16x16;
	Image clockIcon16x16;
	Image scanningIcon16x16;
	Image connectedIcon16x16;
	Image disconnectedIcon16x16;
	Image packagesConnectIcon16x16;
	Image packagesDisconnectIcon16x16;
	Image packagesSyncIcon16x16;
	Image packagesControlIcon16x16;
	Image packagesScenarioIcon16x16;
	Image packagesErrorIcon16x16;
	Image facLogo120x120;
	
	//### Files ################################################################################
	
	File deviceConnectedSound;
	File deviceDisconnectedSound;
	
	
	//### Configurations #######################################################################
	
	//Normally in a separate class!!!!!!
	BooleanProperty soundOn;
	BooleanProperty showSyncPackages;
	BooleanProperty showControlPackages;
	BooleanProperty showScenarioPackages;
	BooleanProperty showConnectPackages;
	BooleanProperty showDisconnectPackages;
	BooleanProperty showErrorPackages;
	
//### CONSTRUCTORS #########################################################################################################################
	
	MainStageResources() {
		
		//### Texts ################################################################################
		
		//Stage title:
		this.stageTitle         = "Flee and Catch - Backend";
		//Label texts:
		this.lblIPAddText       = "IP Address:";
		this.lblPortText        = "Port:";
		this.lblDevicesText     = "Devices:";
		this.lblRobotsText      = "Robots:";
		this.lblAppsText        = "Apps:";
		this.lblScenariosText   = "Scenarios:";
		//Titled border pane titles:
		this.tbpBackendInfoText = "Backend Information";
		this.tbpDevicesInfoText = "Device Information";
		//Texts of the tree view item:
		this.triDevicesText     = "Devices";
		this.triAllText         = "All";
		this.triAllRobotsText   = "Robots";
		this.triAllAppsText     = "Apps";
		this.triScenariosText   = "Scenarios";
		this.triAppText         = "App ID: ";
		this.triRobotText       = "Robot ID: ";
		this.triScenarioText    = "Scenario ID: ";
		//Menu bar texts:
		this.mnuProgramText     = "Program";
		this.mniExitText        = "Exit";
		this.mnuSettingsText    = "Settings";
		this.cmiSoundText       = "Sound";
		this.cmiPackagesSyncText = "Syncronistation Packages";
		this.cmiPackagesControlText = "Control Packages";
		this.cmiPackagesScenarioText = "Scenario Packages";
		this.cmiPackagesConnectText = "Connect Packages";
		this.cmiPackagesDisconnectText = "Disconnect Packages";
		this.cmiPackagesErrorText = "Error Packages";
		//Status bar messages:
		this.stbMsgScanning     = "Waiting for Devices...";
		this.stbMsgDeviceConnected = "New device has been connected!";
		this.stbMsgDeviceDisconnected = "Device has been disconnected!";
		this.lblStbPackagesConnectValue = new SimpleStringProperty("0");
		this.lblStbPackagesDisconnectValue = new SimpleStringProperty("0");
		this.lblStbPackagesSyncValue = new SimpleStringProperty("0");
		this.lblStbPackagesControlValue = new SimpleStringProperty("0");
		this.lblStbPackagesScenarioValue = new SimpleStringProperty("0");
		this.lblStbPackagesErrorValue = new SimpleStringProperty("0");
		//Close-Dialog-Texts:
		this.cpdTitle           = "Flee and Catch - Backend";
		this.cpdHeaderText      = "Exit program?";
		this.cpdContentText     = "Do you really want to exit the program?\n"
							    + "All processes and connections are terminated!\n\n";
		this.cpdBtnApplyText    = "Exit program";
		this.cpdBtnCancelText   = "Cancel";
		
		//### Images ###############################################################################
		
		this.fleeAndCatchLogo16x16       = new Image("icons/fleeAndCatchLogo16x16.png");
		this.fleeAndCatchLogo24x24       = new Image("icons/fleeAndCatchLogo24x24.png");
		this.fleeAndCatchLogo32x32       = new Image("icons/fleeAndCatchLogo32x32.png");
		this.fleeAndCatchLogo48x48       = new Image("icons/fleeAndCatchLogo48x48.png");
		this.fleeAndCatchLogo64x64       = new Image("icons/fleeAndCatchLogo64x64.png");
		this.fleeAndCatchLogo128x128     = new Image("icons/fleeAndCatchLogo128x128.png");
		this.fleeAndCatchLogo256x256     = new Image("icons/fleeAndCatchLogo256x256.png");
		
		this.programIcon16x16            = new Image("icons/program16x16.png");
		this.exitIcon16x16               = new Image("icons/exit16x16.png");
		this.settingsIcon16x16           = new Image("icons/settings16x16.png");
		this.soundIcon16x16              = new Image("icons/sound16x16.png");
		this.soundMuteIcon16x16          = new Image("icons/soundMute16x16.png");
		this.deviceIcon16x16             = new Image("icons/device16x16.png");
		this.allIcon16x16                = new Image("icons/all16x16.png");
		this.robotIcon16x16              = new Image("icons/robot16x16.png");
		this.appIcon16x16                = new Image("icons/app16x16.png");
		this.scenariosIcon16x16          = new Image("icons/scenario16x16.png");
		this.ipaddIcon16x16              = new Image("icons/ipadd16x16.png");
		this.portIcon16x16               = new Image("icons/port16x16.png");
		this.backendIcon16x16            = new Image("icons/backend16x16.png");
		this.clockIcon16x16              = new Image("icons/clock16x16.png");
		this.connectedIcon16x16          = new Image("icons/connected16x16.png");
		this.disconnectedIcon16x16       = new Image("icons/disconnected16x16.png");
		this.scanningIcon16x16           = new Image("icons/scanning16x16.png");
		this.packagesConnectIcon16x16    = new Image("icons/packageConnect16x16.png");
		this.packagesConnectOffIcon16x16 = new Image("icons/packageConnectDisabled16x16.png");
		this.packagesDisconnectIcon16x16 = new Image("icons/packageDisconnect16x16.png");
		this.packagesDisconnectOffIcon16x16 = new Image("icons/packageDisconnectDisabled16x16.png");
		this.packagesSyncIcon16x16       = new Image("icons/packageSync16x16.png");
		this.packagesSyncOffIcon16x16       = new Image("icons/packageSyncDisabled16x16.png");
		this.packagesControlIcon16x16    = new Image("icons/packageControl16x16.png");
		this.packagesControlOffIcon16x16    = new Image("icons/packageControlDisabled16x16.png");
		this.packagesScenarioIcon16x16   = new Image("icons/packageScenario16x16.png");
		this.packagesScenarioOffIcon16x16   = new Image("icons/packageScenarioDisabled16x16.png");
		this.packagesErrorIcon16x16      = new Image("icons/packageError16x16.png");
		this.packagesErrorOffIcon16x16      = new Image("icons/packageErrorDisabled16x16.png");
		this.facLogo120x120              = new Image("images/logo120x120.png");
		
		
		//### Files ################################################################################
		
		this.deviceConnectedSound    = new File("res/sounds/deviceConnectedSound.wav");
		this.deviceDisconnectedSound = new File("res/sounds/deviceDisconnectedSound.wav");
		
		
		//### Configurations #######################################################################
		
		this.soundOn = new SimpleBooleanProperty(true);
		this.showSyncPackages = new SimpleBooleanProperty(true);
		this.showControlPackages = new SimpleBooleanProperty(true);
		this.showScenarioPackages = new SimpleBooleanProperty(true);
		this.showConnectPackages = new SimpleBooleanProperty(true);
		this.showDisconnectPackages = new SimpleBooleanProperty(true);
		this.showErrorPackages = new SimpleBooleanProperty(true);
	}
	
//##########################################################################################################################################
}
//### EOF ##################################################################################################################################