package flee_and_catch.backend.communication.command.device;

public abstract class Device {
	
	protected boolean active;
	
	public Device(boolean pActive) {
		this.active = pActive;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
