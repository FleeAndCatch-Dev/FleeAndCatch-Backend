package flee_and_catch.backend.communication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.util.Objects;

public class Interpreter {

	private Client theClient;
	private boolean opened;

	public Interpreter(Client pClient){
		theClient = pClient;
		opened = true;
		Thread interpretThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					interpret();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		interpretThread.start();
	}
	
	private void interpret() throws InterruptedException, ParseException{
		JSONParser parser = new JSONParser();
		
		while(opened){
			if(theClient.getCommandList().size() > 0){
				JSONObject jsonObject = new JSONObject();
				jsonObject = (JSONObject) parser.parse(theClient.getCommandList().get(0));
				if(Objects.equals((String) jsonObject.get("apiid"), "@@fleeandcatch@@")){
					if(Objects.equals((String) jsonObject.get("id"), "Connection")){
						connection(theClient.getCommandList().get(0));
					}
					else if (Objects.equals((String) jsonObject.get("id"), "Test")) {
						
					}
				}
				else {
					
				}
				theClient.getCommandList().remove(theClient.getCommandList().get(0));
			}
			Thread.sleep(10);
			//dataString = new String(value);
			//System.out.println(value);
			
			/*JSONParser parser = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			jsonObject = (JSONObject) parser.parse(dataString);
			if(Objects.equals((String) jsonObject.get("id"), "Connection")) {
				if(Objects.equals((String) jsonObject.get("type"), "SetType")) {
					jsonObject = (JSONObject) jsonObject.get("client");
					String type = String.valueOf(jsonObject.get("type"));
					clients.get(pId).setType(type);
				}
			}*/
		}
	}
	
	public void close(){
		opened = false;
	}
	
	private void connection(String pCommand) throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) parser.parse(pCommand);
		
		if(Objects.equals((String) jsonObject.get("type"), "SetType")){
			jsonObject = (JSONObject) jsonObject.get("client");
			String type = String.valueOf(jsonObject.get("type"));
			theClient.setType(type);
		}
	}
	
	public boolean isOpened() {
		return opened;
	}
}
