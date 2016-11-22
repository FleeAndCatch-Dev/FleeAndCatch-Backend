package flee_and_catch.backend.communication;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.exception.ParseCommand;
public class Interpreter {

	private Server server;
	private JSONParser parser;
	
	/**
	 * <h1>Constructor</h1>
	 * Creates an object of the class interpreter.
	 * 
	 * @author ThunderSL94
	 */
	public Interpreter(Server pServer){
		this.server = pServer;
		this.parser = new JSONParser();
	}
	
	/**
	 * <h1>Constructor</h1>
	 * Interpret the received command and returns the right string with values or send a new command to the server.
	 * 
	 * @param pCommand Received command from the server
	 * @return
	 * @throws ParseException
	 * @throws ParseCommand
	 * 
	 * @author ThunderSL94
	 * @throws IOException 
	 */
	public String interpret(String pCommand) throws ParseException, ParseCommand, IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) parser.parse(pCommand);
		if(Objects.equals((String) jsonObject.get("apiid"), "@@fleeandcatch@@")){
			char[] typeArray = ((String) jsonObject.get("type")).toCharArray();
			String typeCmd = String.valueOf(typeArray, 0, 3);
			if(Objects.equals("Get", typeCmd)){
				return null;
			}
			else if(Objects.equals("Set", typeCmd)){
				String id = Character.toLowerCase(((String) jsonObject.get("id")).toCharArray()[0]) + ((String) jsonObject.get("id")).substring(1);
				if(Objects.equals("client", id)){
					jsonObject = (JSONObject) jsonObject.get(id);					
					String type = Character.toLowerCase(String.valueOf(typeArray, 3, typeArray.length - 3).toCharArray()[0]) + String.valueOf(typeArray, 3, typeArray.length - 3).substring(1);
					if(Objects.equals("type", type)){
						return (String) jsonObject.get(type);
					}
				}	
			}
			else if(Objects.equals("Disconnect", new String(typeArray))){
				String id = Character.toLowerCase(((String) jsonObject.get("id")).toCharArray()[0]) + ((String) jsonObject.get("id")).substring(1);
				if(Objects.equals("client", id)){
					jsonObject = (JSONObject) jsonObject.get(id);	
					Object clientid = jsonObject.get("id");
					Client client = server.getClients().get(Integer.valueOf(clientid.toString()));
					String cmd = "{\"id\":\"Client\",\"type\":\"Disconnect\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(client.getId()) + ",\"type\":\"" + client.getType().toString() + "\"}}";
					server.sendCmd(client, cmd);
					server.removeClient(client);
					return null;
				}
			}
		}		
		throw new ParseCommand();
	}
}
