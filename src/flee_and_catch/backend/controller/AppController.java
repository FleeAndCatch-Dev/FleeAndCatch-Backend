package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.List;

import flee_and_catch.backend.communication.command.device.app.App;

public final class AppController {
	private static ArrayList<App> apps = new ArrayList<App>();

	public static List<App> getApps() {
		return apps;
	}
}
