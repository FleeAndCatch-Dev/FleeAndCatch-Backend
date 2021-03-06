package flee_and_catch.backend.communication.command.szenario;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class SzenarioAdapter implements JsonDeserializer<Szenario> {
	@Override
	public Szenario deserialize(JsonElement json, Type szenario, JsonDeserializationContext context) throws JsonParseException {
		String myType = json.getAsJsonObject().get("type").getAsString();
        switch (myType) {
            case "Control": return context.deserialize(json, Control.class);
            case "Synchron": return context.deserialize(json, Synchron.class);
            default: return null;
        }
	}
}