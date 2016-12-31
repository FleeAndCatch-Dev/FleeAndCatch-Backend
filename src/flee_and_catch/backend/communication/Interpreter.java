package flee_and_catch.backend.communication;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.communication.command.Command;
import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.Connection;
import flee_and_catch.backend.communication.command.ConnectionType;
import flee_and_catch.backend.communication.command.Synchronization;
import flee_and_catch.backend.communication.command.SynchronizationType;
import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.communication.command.Control;
import flee_and_catch.backend.communication.command.ControlType;
import flee_and_catch.backend.view.View;

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
		CommandType id = CommandType.valueOf((String) jsonCommand.get("id"));
		
		switch (id) {
			case Connection:
				connection(jsonCommand);
				return;
			case Synchronisation:
				synchronisation(jsonCommand);
				return;
			case Control:
				control(jsonCommand);
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
		ConnectionType type = ConnectionType.valueOf((String) pCommand.get("type"));
		Connection command = gson.fromJson(pCommand.toString(), Connection.class);
		IdentificationType clienttype;
		switch(type){
			case SetType:
				client.getIdentification().setType(command.getIdentification().getType());
				clienttype = IdentificationType.valueOf(command.getIdentification().getType());			
				switch(clienttype){
				case App:
					App app = new App();
					client.setDevice(app);
					//Add app
					//AppController.getApps().add(new App(command.getIdentification()));
					View.setMobileDevice(AppController.getApps().size());
					break;
				case Robot:
					//Add robot
					//RobotType robottype = RobotType.valueOf(command.getIdentification().getSubtype());
					client.setDevice(command.getDevice());
					RobotController.getRobots().add((Robot)command.getDevice());	
					View.setRobots(RobotController.getRobots().size());
					break;
				}
				return;
			case Disconnect:
				Connection cmd = new Connection(CommandType.Connection.toString(), ConnectionType.Disconnect.toString(), client.getIdentification(), command.getDevice());
				
				Server.sendCmd(client, cmd.getCommand());
				clienttype = IdentificationType.valueOf(command.getIdentification().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getIdentification().getId() == command.getIdentification().getId()){
							AppController.getApps().remove(AppController.getApps().get(i));
							break;
						}
					}
					break;
				case Robot:
					//Remove robot
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getIdentification().getId()){
							RobotController.getRobots().remove(RobotController.getRobots().get(i));
							break;
						}
					}
					break;
				}
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
		SynchronizationType type = SynchronizationType.valueOf((String) pCommand.get("type"));
		//Synchronization command = gson.fromJson(pCommand.toString(), Synchronization.class);
		
		switch(type){
			case GetRobots:
				Synchronization cmd = new Synchronization(CommandType.Synchronisation.toString(), SynchronizationType.SetRobots.toString(), client.getIdentification(), RobotController.getRobots());
				Server.sendCmd(client, cmd.getCommand());
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
	
	/**
	 * <h1>Parse control</h1>
	 * Parse of a synchronization command.
	 * 
	 * @param pCommand Command as json object.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private void control(JSONObject pCommand) throws Exception{
		if(pCommand == null) throw new NullPointerException();
		ControlType type = ControlType.valueOf((String) pCommand.get("type"));
		Control command = gson.fromJson(pCommand.toString(), Control.class);
		Command cmd;
		
		switch(type){
			case Begin:
				RobotController.changeActive(command.getRobot(), true);
				cmd = new Control(CommandType.Control.toString(), ControlType.Begin.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(client, cmd.getCommand());
				break;
			case End:
				RobotController.changeActive(command.getRobot(), false);
				cmd = new Control(CommandType.Control.toString(), ControlType.End.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(client, cmd.getCommand());
				break;
			case Start:
				break;
			case Stop:
				break;
			case Control:
				break;
			default:
				throw new Exception("Argument out of range");
		}
		
		cmd = new Synchronization(CommandType.Synchronisation.toString(), SynchronizationType.SetRobots.toString(), client.getIdentification(), RobotController.getRobots());
		Server.sendCmd(client, cmd.getCommand());
		return;
	}
}
