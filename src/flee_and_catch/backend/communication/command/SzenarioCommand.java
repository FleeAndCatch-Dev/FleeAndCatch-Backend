package flee_and_catch.backend.communication.command;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.identification.ClientIdentification;
import flee_and_catch.backend.communication.command.szenario.Szenario;

public class SzenarioCommand extends Command {
	private Szenario szenario;

	public SzenarioCommand(String pId, String pType, ClientIdentification pIdentification, Szenario pSzenario) {
		super(pId, pType, pIdentification);
		this.szenario = pSzenario;
	}

	@Override
	public String getCommand() throws JSONException {		
		JSONObject command = new JSONObject();
		command.put("id", id);
		command.put("type", type);
		command.put("apiid", apiid);
		command.put("errorhandling", errorhandling);
		command.put("identification", identification.getJSONObject());
		command.put("szenario", szenario.getJSONObject());
		
		return command.toString();
	}

	public Szenario getSzenario() {
		return szenario;
	}
}
