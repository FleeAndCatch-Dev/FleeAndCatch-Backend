package flee_and_catch.backend.view;

import javax.security.auth.Refreshable;

public final class View {
	
	private static int robots = 0;
	private static int threeWheelDrives = 0;
	private static int fourWheelDrives = 0;
	private static int chainDrives = 0;
	private static int mobileDevices = 0;
	
	private View() {
		
	};
	
	public static void setRobots(int robots) {
		View.robots = robots;
		View.refresh();
	}
	
	public static void setThreeWheelDrive(int threeWheelDrives) {
		View.threeWheelDrives = threeWheelDrives;
		View.refresh();
	}
	
	public static void setFourWheelDrive(int fourWheelDrives) {
		View.fourWheelDrives = fourWheelDrives;
		View.refresh();
	}
	
	public static void setChainDrive(int chainDrives) {
		View.chainDrives = chainDrives;
		View.refresh();
	}
	
	public static void setMobileDevice(int mobileDevices) {
		View.mobileDevices = mobileDevices;
		View.refresh();
	}
	
	private static void refresh() {
		System.out.println();
		System.out.println("Robots: " + View.robots);
		//System.out.println("ThreeWheelDrives: " + View.threeWheelDrives);
		//System.out.println("FourWheelDrives: " + View.fourWheelDrives);
		//System.out.println("ChainDrives: " + View.chainDrives);
		System.out.println("MobileDevices: " + View.mobileDevices);
		
	}
	
}
