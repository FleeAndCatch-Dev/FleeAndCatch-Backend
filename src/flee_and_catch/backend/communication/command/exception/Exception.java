package flee_and_catch.backend.communication.command.exception;

import flee_and_catch.backend.communication.command.device.Device;

public class Exception {
	private String type;
    private String message;
    private Device device;

    public Exception(String pType, String pMessage, Device pDevice)
    {
        this.type = pType;
        this.message = pMessage;
        this.device = pDevice;
    }

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public Device getDevice() {
		return device;
	}
}
