package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class ExceptionCommand extends Command {
	private flee_and_catch.backend.communication.command.exception.Exception exception;

	public ExceptionCommand(String pId, String pType, ClientIdentification pIdentification, flee_and_catch.backend.communication.command.exception.Exception pException) {
		super(pId, pType, pIdentification);
		this.exception = pException;
	}

	public String getCommand() throws JSONException {
		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		command.put("exception", exception.getJSONObject());
		
		return command.toString();
	}

	public flee_and_catch.backend.communication.command.exception.Exception getException() {
		return exception;
	}
}
