package flee_and_catch.backend.communication;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.app.App;
import flee_and_catch.backend.app.AppController;
import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.connection.Connection;
import flee_and_catch.backend.communication.command.connection.ConnectionType;
import flee_and_catch.backend.communication.command.synchronisation.Synchronisation;
import flee_and_catch.backend.communication.command.synchronisation.SynchronisationType;
import flee_and_catch.backend.robot.RobotController;
import flee_and_catch.backend.robot.ThreeWheelDriveRobot;

public class Interpreter {

	private Client client;
	private Gson gson;
	
	public Interpreter(Client pClient){
		this.client = pClient;
		this.gson = new Gson();
	}

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

	private void connection(JSONObject pCommand) throws Exception {
		if(pCommand == null) throw new NullPointerException();
		ConnectionType.Type type = ConnectionType.Type.valueOf((String) pCommand.get("type"));
		Connection command = gson.fromJson(pCommand.toString(), Connection.class);
		Type clienttype;
		switch(type){
			case SetType:
				client.setType(Type.valueOf(command.getClient().getType()).toString());
				clienttype = Type.valueOf(command.getClient().getType());
				switch(clienttype){
				case App:
					//Add app
					AppController.getApps().add(new App(client.getId()));
					client.setSubtype("null");
					break;
				case Robot:
					//Add robot
					flee_and_catch.backend.robot.Type robottype = flee_and_catch.backend.robot.Type.valueOf(command.getClient().getSubtype());
					switch(robottype){
						case ThreeWheelDrive:
							RobotController.getRobots().add(new ThreeWheelDriveRobot(client.getId(), flee_and_catch.backend.robot.Type.ThreeWheelDrive.toString()));
							client.setSubtype(flee_and_catch.backend.robot.Type.valueOf(command.getClient().getSubtype()).toString());
							break;
					}
					break;
				}
				System.out.println("Type of client: " + client.getId() + " set as " + client.getType().toString());
				return;
			case Disconnect:
				Connection cmd = new Connection(CommandType.Type.Connection.toString(), ConnectionType.Type.Disconnect.toString(), new flee_and_catch.backend.communication.command.connection.Client(client.getId(), client.getType(), client.getSubtype()));
				
				Server.sendCmd(client, cmd.GetCommand());
				clienttype = Type.valueOf(command.getClient().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getId() == client.getId()){
							AppController.getApps().remove(AppController.getApps().get(i));
							break;
						}
					}
					break;
				case Robot:
					//Remove robot
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getId() == client.getId()){
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
	
	private void synchronisation(JSONObject pCommand) throws Exception{
		if(pCommand == null) throw new NullPointerException();
		SynchronisationType.Type type = SynchronisationType.Type.valueOf((String) pCommand.get("type"));
		//Synchronisation command = gson.fromJson(pCommand.toString(), Synchronisation.class);
		
		switch(type){
			case GetRobots:
				Synchronisation cmd = new Synchronisation(CommandType.Type.Synchronisation.toString(), SynchronisationType.Type.SetRobots.toString(), new flee_and_catch.backend.communication.command.connection.Client(client.getId(), client.getType(), client.getSubtype()), RobotController.getRobots());
				Server.sendCmd(client, cmd.GetCommand());
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
}
