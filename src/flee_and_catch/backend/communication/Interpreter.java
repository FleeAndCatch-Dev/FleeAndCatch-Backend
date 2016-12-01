package flee_and_catch.backend.communication;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.app.App;
import flee_and_catch.backend.app.AppController;
import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.Connection;
import flee_and_catch.backend.communication.command.ConnectionType;
import flee_and_catch.backend.communication.command.Identification;
import flee_and_catch.backend.communication.command.Position;
import flee_and_catch.backend.communication.command.Synchronization;
import flee_and_catch.backend.communication.command.SynchronizationType;
import flee_and_catch.backend.component.RobotType;
import flee_and_catch.backend.component.IdentificationType;
import flee_and_catch.backend.component.IdentificationType.Typ;
import flee_and_catch.backend.robot.RobotController;
import flee_and_catch.backend.robot.ThreeWheelDrive;

public class Interpreter {

	private Client client;
	private Gson gson;
	
	/**
	 * <h1>Constructor</h1>
	 * Create an object of the class interpreter.
	 * 
	 * @param pClient Client object.
	 * 
	 * @author ThunderSL94
	 */
	public Interpreter(Client pClient){
		this.client = pClient;
		this.gson = new Gson();
	}

	/**
	 * <h1Parse</h1>
	 * Parse a json string from the server and check the command of the right apiid and the different id.
	 * 
	 * @param pCommand Json command as string.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	public void parse(String pCommand) throws Exception {
		JSONObject jsonCommand = new JSONObject(pCommand);
		
		if(!Objects.equals("@@fleeandcatch@@", (String) jsonCommand.get("apiid")))
			throw new Exception("Wrong apiid in json command");
		CommandType.Type id = CommandType.Type.valueOf((String) jsonCommand.get("id"));
		
		switch (id) {
			case Connection:
				connection(jsonCommand);
				return;
			case Synchronisation:
				synchronisation(jsonCommand);
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}

	/**
	 * <h1>Parse connection</h1>
	 * Parse of a connection command.
	 * 
	 * @param pCommand Command as json object.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private void connection(JSONObject pCommand) throws Exception {
		if(pCommand == null) throw new NullPointerException();
		ConnectionType.Type type = ConnectionType.Type.valueOf((String) pCommand.get("type"));
		Connection command = gson.fromJson(pCommand.toString(), Connection.class);
		IdentificationType.Typ clienttype;
		switch(type){
			case SetType:
				client.setType(command.getIdentification().getType());
				client.setSubtype(command.getIdentification().getSubtype());
				clienttype = IdentificationType.Typ.valueOf(command.getIdentification().getType());			
				switch(clienttype){
				case App:
					//Add app
					AppController.getApps().add(new App(command.getIdentification()));
					break;
				case Robot:
					//Add robot
					RobotType.Type robottype = RobotType.Type.valueOf(command.getIdentification().getSubtype());
					switch(robottype){
						case ThreeWheelDrive:
							RobotController.getRobots().add(new ThreeWheelDrive(command.getIdentification(), new Position(0, 0, 0), 0));	
							break;
					}
					break;
				}
				System.out.println("Type of client: " + client.getId() + " set as " + client.getType().toString());
				return;
			case Disconnect:
				Connection cmd = new Connection(CommandType.Type.Connection.toString(), ConnectionType.Type.Disconnect.toString(), command.getIdentification());
				
				Server.sendCmd(client, cmd.getCommand());
				clienttype = Typ.valueOf(command.getIdentification().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getIdentification().getId() == client.getId()){
							AppController.getApps().remove(AppController.getApps().get(i));
							break;
						}
					}
					break;
				case Robot:
					//Remove robot
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == client.getId()){
							RobotController.getRobots().remove(RobotController.getRobots().get(i));
							break;
						}
					}
					break;
				}
				System.out.println("Client with id: " + client.getId() + ", type: " + client.getType().toString() + " and subtype: " + client.getSubtype() + " disconnected");
				Server.removeClient(client);
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
	
	/**
	 * <h1>Parse synchronization</h1>
	 * Parse of a synchronization command.
	 * 
	 * @param pCommand Command as json object.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private void synchronisation(JSONObject pCommand) throws Exception{
		if(pCommand == null) throw new NullPointerException();
		SynchronizationType.Type type = SynchronizationType.Type.valueOf((String) pCommand.get("type"));
		Synchronization command = gson.fromJson(pCommand.toString(), Synchronization.class);
		
		switch(type){
			case GetRobots:
				Synchronization cmd = new Synchronization(CommandType.Type.Synchronisation.toString(), SynchronizationType.Type.SetRobots.toString(), command.getIdentification(), RobotController.getRobots());
				Server.sendCmd(client, cmd.getCommand());
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
}
