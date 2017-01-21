package flee_and_catch.backend.communication.command.device;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;

public final class DeviceAdapter implements JsonDeserializer<Device> {

	@Override
	public Device deserialize(JsonElement json, Type device, JsonDeserializationContext context) throws JsonParseException {
		JsonObject identification = json.getAsJsonObject().get("identification").getAsJsonObject();
		String myType = identification.getAsJsonObject().get("type").getAsString();
        switch (myType) {
            case "App": return context.deserialize(json, App.class);
            case "Robot": return context.deserialize(json, Robot.class);
            default: return null;
        }
	}

}