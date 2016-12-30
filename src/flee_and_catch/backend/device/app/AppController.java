package flee_and_catch.backend.device.app;

import java.util.ArrayList;
import java.util.List;

public final class AppController {
	private static ArrayList<App> apps = new ArrayList<App>();

	public static List<App> getApps() {
		return apps;
	}
}
