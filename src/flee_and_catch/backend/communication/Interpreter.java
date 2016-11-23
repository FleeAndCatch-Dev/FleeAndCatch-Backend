package flee_and_catch.backend.communication;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.exception.ParseCommand;
public class Interpreter {
	private Server server;
	private Client client;
	private JSONParser parser;
	
	public Interpreter(Server pServer, Client pClient){
		this.parser = new JSONParser();
		this.server = pServer;
		this.client = pClient;
	}
	
	public void parse(String pCommand) throws ParseException, ParseCommand, IOException{
		JSONObject jsonCommand = new JSONObject();
		jsonCommand = (JSONObject) parser.parse(pCommand);
		if(Objects.equals("@@fleeandcatch@@", (String) jsonCommand.get("apiid"))){
			switch (((String) jsonCommand.get("id")).toLowerCase()) {
			case "registration":
				registration(jsonCommand);
				return;
			case "disconnection":
				disconnect(jsonCommand);
				return;
			default:
				break;
			}
		}
		throw new ParseCommand();
	}
	
	private void registration(JSONObject pCommand) throws ParseCommand{
		String type = ((String) pCommand.get("type")).toLowerCase();
		if(Objects.equals("settype", type)){
			JSONObject jsonclient = (JSONObject) pCommand.get("client");
			String clientType = (String) jsonclient.get("type");
			client.setType(ClientType.valueOf(clientType));
			return;
		}
		throw new ParseCommand();
	}
	
	private void disconnect(JSONObject pCommand) throws ParseCommand, IOException, ParseException{
		JSONObject jsonclient = (JSONObject) pCommand.get("client");
		String type = ((String) pCommand.get("type")).toLowerCase();
		if(Objects.equals("disconnect", type)){
			Object clientid = jsonclient.get("id");
			Client client = server.getClients().get(Integer.valueOf(clientid.toString()));
			String cmd = "{\"id\":\"Disconnection\",\"type\":\"Disconnect\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(client.getId()) + ",\"type\":\"" + client.getType().toString() + "\"}}";
			server.sendCmd(client,  cmd);
			server.removeClient(client);
			return;
		}
		throw new ParseCommand();
	}
}
