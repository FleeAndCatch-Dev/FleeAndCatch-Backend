package flee_and_catch.backend.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.communication.command.Command;
import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.Connection;
import flee_and_catch.backend.communication.command.ConnectionType;
import flee_and_catch.backend.communication.command.Synchronization;
import flee_and_catch.backend.communication.command.SynchronizationType;
import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.device.DeviceAdapter;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.communication.command.Control;
import flee_and_catch.backend.communication.command.ControlType;

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
			case Synchronization:
				synchronization(jsonCommand);
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
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();		
		
		Connection command = localgson.fromJson(pCommand.toString(), Connection.class);
		IdentificationType clienttype;
		switch(type){
			case Connect:
				switch (IdentificationType.valueOf(command.getIdentification().getType())) {
				case App:
					App app = (App)command.getDevice();
					app.getIdentification().setId(client.getIdentification().getId());
					command.getIdentification().setId(client.getIdentification().getId());
					client.setIdentification(command.getIdentification());
					client.setDevice(app);
					AppController.getApps().add(app);
					break;
				case Robot:
					Robot robot = (Robot)command.getDevice();
					robot.getIdentification().setId(client.getIdentification().getId());
					command.getIdentification().setId(client.getIdentification().getId());
					client.setIdentification(command.getIdentification());
					client.setDevice(robot);
					RobotController.getRobots().add(robot);
					break;
				default:
					throw new Exception("Undefined connection");
				}	
				command = new Connection(CommandType.Connection.toString(), ConnectionType.Connect.toString(), client.getIdentification(), client.getDevice());
				Server.sendCmd(client, command.getCommand());
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
				default:
					throw new Exception("Undefined disconnection");
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
	private void synchronization(JSONObject pCommand) throws Exception{
		if(pCommand == null) throw new NullPointerException();
		SynchronizationType type = SynchronizationType.valueOf((String) pCommand.get("type"));
		Synchronization command = gson.fromJson(pCommand.toString(), Synchronization.class);
		
		switch(type){
			case All:
				if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
					Synchronization cmd = new Synchronization(CommandType.Synchronization.toString(), SynchronizationType.All.toString(), client.getIdentification(), RobotController.getRobots());
					Server.sendCmd(client, cmd.getCommand());
				}
				else
					throw new Exception("Not implemented");
				return;
			case Current:
				if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
					ArrayList<Robot> robotlist = new ArrayList<Robot>();
					for(int i=0;i<RobotController.getRobots().size();i++){
						for(int j=0;j<command.getRobots().size();j++){
							if(RobotController.getRobots().get(i).getIdentification() == command.getRobots().get(j).getIdentification())
								robotlist.add(RobotController.getRobots().get(i));
						}
					}
					Synchronization cmd = new Synchronization(CommandType.Synchronization.toString(), SynchronizationType.All.toString(), client.getIdentification(), robotlist);
					Server.sendCmd(client, cmd.getCommand());
				}
				else if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.Robot) {
					for (int i = 0; i < RobotController.getRobots().size(); i++) {
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getRobots().get(0).getIdentification().getId())
							RobotController.getRobots().set(i, command.getRobots().get(0));
					}
				}
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
		
		Client localclient = null;
		for(int i=0;i<Server.getClients().size();i++){
			if(command.getRobot().getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
				localclient = Server.getClients().get(i);
				break;
			}
		}
		
		switch(type){
			case Begin:
				RobotController.changeActive(command.getRobot(), true);
				cmd = new Control(CommandType.Control.toString(), ControlType.Begin.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(localclient, cmd.getCommand());
				return;
			case End:
				RobotController.changeActive(command.getRobot(), false);
				cmd = new Control(CommandType.Control.toString(), ControlType.End.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(localclient, cmd.getCommand());
				return;
			case Start:
				cmd = new Control(CommandType.Control.toString(), ControlType.Start.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(localclient, cmd.getCommand());
				return;
			case Stop:
				cmd = new Control(CommandType.Control.toString(), ControlType.Stop.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(localclient, cmd.getCommand());
				return;
			case Control:
				cmd = new Control(CommandType.Control.toString(), ControlType.Control.toString(), client.getIdentification(), command.getRobot(), command.getSteering());
				Server.sendCmd(localclient, cmd.getCommand());
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
}
