package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.device.app.App;

public final class AppController {
	private static ArrayList<App> apps = new ArrayList<App>();
	private static Lock appsLock = new ReentrantLock();

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
