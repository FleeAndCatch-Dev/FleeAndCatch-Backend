//### MainStageResources.java ##############################################################################################################

package flee_and_catch.backend.view.stage;

import java.io.File;

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
	//Menu bar texts:
	String mnuProgramText;
	String mniExitText;
	//Status bar message:
	String stbMsgScanning;
	String stbMsgDeviceConnected;
	String stbMsgDeviceDisconnected;
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
	Image exitIcon16x16;
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
	Image facLogo120x120;
	
	//### Files ################################################################################
	
	File deviceConnectedSound;
	File deviceDisconnectedSound;
	
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
		//Menu bar texts:
		this.mnuProgramText     = "Program";
		this.mniExitText        = "Exit";
		//Status bar messages:
		this.stbMsgScanning     = "Waiting for Devices...";
		this.stbMsgDeviceConnected = "New device has been connected!";
		this.stbMsgDeviceDisconnected = "Device has been disconnected!";
		
		//Close-Dialog-Texts:
		this.cpdTitle           = "Flee and Catch - Backend";
		this.cpdHeaderText      = "Exit program?";
		this.cpdContentText     = "Do you really want to exit the program?\n"
							    + "All processes and connections are terminated!\n\n";
		this.cpdBtnApplyText    = "Exit program";
		this.cpdBtnCancelText   = "Cancel";
		
		//### Images ###############################################################################
		
		this.fleeAndCatchLogo16x16   = new Image("icons/fleeAndCatchLogo16x16.png");
		this.fleeAndCatchLogo24x24   = new Image("icons/fleeAndCatchLogo24x24.png");
		this.fleeAndCatchLogo32x32   = new Image("icons/fleeAndCatchLogo32x32.png");
		this.fleeAndCatchLogo48x48   = new Image("icons/fleeAndCatchLogo48x48.png");
		this.fleeAndCatchLogo64x64   = new Image("icons/fleeAndCatchLogo64x64.png");
		this.fleeAndCatchLogo128x128 = new Image("icons/fleeAndCatchLogo128x128.png");
		this.fleeAndCatchLogo256x256 = new Image("icons/fleeAndCatchLogo256x256.png");
		
		this.exitIcon16x16           = new Image("icons/exit16x16.png");
		this.deviceIcon16x16         = new Image("icons/device16x16.png");
		this.allIcon16x16            = new Image("icons/all16x16.png");
		this.robotIcon16x16          = new Image("icons/robot16x16.png");
		this.appIcon16x16            = new Image("icons/app16x16.png");
		this.scenariosIcon16x16      = new Image("icons/scenario16x16.png");
		this.ipaddIcon16x16          = new Image("icons/ipadd16x16.png");
		this.portIcon16x16           = new Image("icons/port16x16.png");
		this.backendIcon16x16        = new Image("icons/backend16x16.png");
		this.clockIcon16x16          = new Image("icons/clock16x16.png");
		this.connectedIcon16x16      = new Image("icons/connected16x16.png");
		this.disconnectedIcon16x16   = new Image("icons/disconnected16x16.png");
		this.scanningIcon16x16       = new Image("icons/scanning16x16.png");
		this.facLogo120x120          = new Image("images/logo120x120.png");
		
		
		//### Files ################################################################################
		
		this.deviceConnectedSound    = new File("res/sounds/deviceConnectedSound.wav");
		this.deviceDisconnectedSound = new File("res/sounds/deviceDisconnectedSound.wav");
	}
	
//##########################################################################################################################################
}
//### EOF ##################################################################################################################################