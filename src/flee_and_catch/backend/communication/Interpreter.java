package flee_and_catch.backend.communication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.exception.ParseCommand;

public class Interpreter {

	private JSONParser parser;
	
	public Interpreter(){
		this.parser = new JSONParser();
	}
	
	public String interpret(String receiveCmd) throws ParseException, ParseCommand {
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) parser.parse(receiveCmd);
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
		}		
		throw new ParseCommand();
	}
}
