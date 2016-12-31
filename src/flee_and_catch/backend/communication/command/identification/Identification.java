package flee_and_catch.backend.communication.command.identification;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Identification {
	protected int id;
	
	public abstract JSONObject getJSONObject() throws JSONException;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
