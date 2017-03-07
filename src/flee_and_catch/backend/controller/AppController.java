package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.view.ViewController;

public final class AppController {
	private static ArrayList<App> apps = new ArrayList<App>();
	private static Lock appsLock = new ReentrantLock();

	public static void changeActive(App pApp, boolean pState){
		appsLock.lock();
		pApp.setActive(pState);
		for(int i=0; i<AppController.getApps().size(); i++){
			if(AppController.getApps().get(i).getIdentification().getId() == pApp.getIdentification().getId()){
				//Check if robots equal
				AppController.getApps().get(i).setActive(pState);
				if(!pState)
					AppController.getApps().get(i).setRobotId(-1);
			}
		}
		appsLock.unlock();
	}
	
	public static void addNew(App pApp){		
		appsLock.lock();
		apps.add(pApp);
		appsLock.unlock();
		
		//Set number of robots in view:
		//ViewController.setNumberOfDevices(Server.getClients().size());
		ViewController.setNumberOfApps(AppController.getApps().size());
	}
	
	public static void remove(App pApp){		
		appsLock.lock();
		apps.remove(pApp);
		appsLock.unlock();
		
		//Set number of robots in view:
		//ViewController.setNumberOfDevices(Server.getClients().size());
		ViewController.setNumberOfApps(AppController.getApps().size());
	}
	
	public static List<App> getApps() {
		appsLock.lock();
		ArrayList<App> appList = new ArrayList<App>(apps);
		appsLock.unlock();
		return appList;
	}

	public static void setApps(ArrayList<App> apps) {
		appsLock.lock();
		AppController.apps = apps;
		appsLock.unlock();
	}
	
}
