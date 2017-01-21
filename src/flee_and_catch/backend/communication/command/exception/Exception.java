package flee_and_catch.backend.communication.command.exception;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject getJSONObject() throws JSONException{
		JSONObject jsonException = new JSONObject();
		jsonException.put("type", type);
		jsonException.put("message", message);
		jsonException.put("device", device.getJSONObject());

		return jsonException;
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
