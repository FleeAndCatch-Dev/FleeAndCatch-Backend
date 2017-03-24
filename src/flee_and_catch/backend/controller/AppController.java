package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.view.ViewController;

public final class AppController {
	private static ArrayList<App> apps = new ArrayList<App>();
	private static Lock appsLock = new ReentrantLock();

	public static void changeActive(App pApp, boolean pState){
		appsLock.lock();
		for(int i=0; i<AppController.getApps().size(); i++){
			if(AppController.getApps().get(i).getIdentification().getId() == pApp.getIdentification().getId()){
				//Check if robots equal
				AppController.getApps().get(i).setActive(pState);
			}
		}
		appsLock.unlock();
	}
	
	public static void addNew(App pApp){		
		appsLock.lock();
		apps.add(pApp);
		sort();
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
	
	private static void sort(){
		App value;
        for (int i = 0; i < apps.size() - 1; i++) { 
            if (apps.get(i).getIdentification().getId() < apps.get(i + 1).getIdentification().getId()) { 
                continue; 
            } 
            value = apps.get(i); 
            apps.set(i, apps.get(i + 1));
            apps.set(i + 1, value);
            sort(); 
        }
        return;
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
