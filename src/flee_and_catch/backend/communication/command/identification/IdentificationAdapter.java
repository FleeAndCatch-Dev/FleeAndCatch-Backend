package flee_and_catch.backend.communication.command.identification;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class IdentificationAdapter implements JsonDeserializer<Identification> {
	@Override
	public Identification deserialize(JsonElement json, Type identification, JsonDeserializationContext context) throws JsonParseException {
		
		if(identification.equals(ClientIdentification.class)){
			return context.deserialize(json, ClientIdentification.class);
		}
		else if(identification.equals(AppIdentification.class)){
			return context.deserialize(json, AppIdentification.class);
		}
		else if (identification.equals(RobotIdentification.class)) {
			return context.deserialize(json, RobotIdentification.class);
		}
		return null;
	}
}
