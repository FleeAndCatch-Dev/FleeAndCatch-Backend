package flee_and_catch.backend.communication.command;

public class ExceptionCommand {
	/*private int exceptionid;
	private String message;
	private Device device;
	
	protected ExceptionCommand(String pId, String pType, ClientIdentification pIdentification, int pExceptionId, Device pDevice) {
		super(pId, pType, pIdentification);
		this.exceptionid = pExceptionId;
		this.message = ExceptionCommandType.values()[exceptionid].toString();
		this.device = pDevice;
	}

	@Override
	public String getCommand() throws JSONException {
		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		command.put("exceptionid", exceptionid);
		command.put("message", message);
		command.put("device", device.getJSONObject());
		
		return command.toString();
	}

	public int getExceptionid() {
		return exceptionid;
	}

	public String getMessage() {
		return message;
	}

	public Device getDevice() {
		return device;
	}*/
}
