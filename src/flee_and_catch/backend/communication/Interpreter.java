package flee_and_catch.backend.communication;

import java.io.IOException;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.ConnectionCommand;
import flee_and_catch.backend.communication.command.ConnectionCommandType;
import flee_and_catch.backend.communication.command.ControlCommand;
import flee_and_catch.backend.communication.command.SynchronizationCommand;
import flee_and_catch.backend.communication.command.SynchronizationCommandType;
import flee_and_catch.backend.communication.command.SzenarioCommand;
import flee_and_catch.backend.communication.command.SzenarioCommandType;
import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.device.DeviceAdapter;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.szenario.Control;
import flee_and_catch.backend.communication.command.szenario.ControlType;
import flee_and_catch.backend.communication.command.szenario.Szenario;
import flee_and_catch.backend.communication.command.szenario.SzenarioAdapter;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.controller.SzenarioController;

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
			case Szenario:
				szenario(jsonCommand);
				return;
			case Exception:
				exception(jsonCommand);
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
		ConnectionCommandType type = ConnectionCommandType.valueOf((String) pCommand.get("type"));
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();		
		
		ConnectionCommand command = localgson.fromJson(pCommand.toString(), ConnectionCommand.class);
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
					AppController.addNew(app);
					break;
				case Robot:
					Robot robot = (Robot)command.getDevice();
					robot.getIdentification().setId(client.getIdentification().getId());
					command.getIdentification().setId(client.getIdentification().getId());
					client.setIdentification(command.getIdentification());
					client.setDevice(robot);
					RobotController.addNew(robot);
					break;
				default:
					throw new Exception("Undefined connection");
				}	
				Gson gson = new Gson();
				command = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Connect.toString(), client.getIdentification(), client.getDevice());
				Server.sendCmd(client, gson.toJson(command));
				return;
			case Disconnect:
				Gson gson1 = new Gson();
				ConnectionCommand cmd = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Disconnect.toString(), client.getIdentification(), command.getDevice());				
				Server.sendCmd(client, gson1.toJson(cmd));
				clienttype = IdentificationType.valueOf(command.getIdentification().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getIdentification().getId() == command.getIdentification().getId()){
							App app = new App(AppController.getApps().get(i));
							AppController.remove(app);
							break;
						}
					}
					break;
				case Robot:			
					//Remove robot
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getIdentification().getId()){
							Robot robot = new Robot(RobotController.getRobots().get(i));
							RobotController.remove(robot);
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
		SynchronizationCommandType type = SynchronizationCommandType.valueOf((String) pCommand.get("type"));
		SynchronizationCommand command = gson.fromJson(pCommand.toString(), SynchronizationCommand.class);
		
		switch(type){
			case All:
				if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
					Gson gson = new Gson();
					SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.All.toString(), client.getIdentification(), RobotController.getRobots());
					Server.sendCmd(client, gson.toJson(cmd));
				}
				else
					throw new Exception("Not implemented");
				return;
			case Current:
				//New update from a robot
				if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.Robot){
					//Get the szenario of the robot
					Szenario szenario = null;
					for(int i=0;i<SzenarioController.getSzenarios().size();i++){
						for(int j=0;j<SzenarioController.getSzenarios().get(i).getRobots().size();j++){
							if(SzenarioController.getSzenarios().get(i).getRobots().get(j).getIdentification().getId() == command.getIdentification().getId()){
								szenario = SzenarioController.getSzenarios().get(i);
								break;
							}
						}
					}
					if(szenario != null){
						for(int i=0;i<szenario.getApps().size();i++){
							//Get the client for the app
							Client localclient = null;
							for(int j=0;j<Server.getClients().size();j++){
								if(szenario.getApps().get(i).getIdentification().getId() == Server.getClients().get(j).getIdentification().getId()){
									localclient = Server.getClients().get(j);
									break;
								}
							}
							if(localclient != null){
								Gson gson = new Gson();
								SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.Current.toString(), client.getIdentification(), command.getRobots());
								Server.sendCmd(localclient, gson.toJson(cmd));
							}
						}
					}
				}
				
				/*Client localclient = null;
				for(int i=0;i<Server.getClients().size();i++){
					if(pCommand.getSzenario().getRobots().get(0).getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
						localclient = Server.getClients().get(i);
						break;
					}
				}*/
				
				/*if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
					ArrayList<Robot> robotlist = new ArrayList<Robot>();
					for(int i=0;i<RobotController.getRobots().size();i++){
						for(int j=0;j<command.getRobots().size();j++){
							if(RobotController.getRobots().get(i).getIdentification() == command.getRobots().get(j).getIdentification())
								robotlist.add(RobotController.getRobots().get(i));
						}
					}
					Gson gson = new Gson();
					SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.All.toString(), client.getIdentification(), robotlist);
					Server.sendCmd(client, gson.toJson(cmd));
				}
				else if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.Robot) {
					for (int i = 0; i < RobotController.getRobots().size(); i++) {
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getRobots().get(0).getIdentification().getId()){
							ArrayList<Robot> robotList = new ArrayList<Robot>(RobotController.getRobots());
							robotList.set(i, command.getRobots().get(0));
							RobotController.setRobots(robotList);
						}
					}
				}*/
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
	
	/**
	 * <h1>Parse control</h1>
	 * Parse of a szenario command.
	 * 
	 * @param pCommand Command as json object.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private void szenario(JSONObject pCommand) throws Exception{
		if(pCommand == null) throw new NullPointerException();
		SzenarioCommandType type = SzenarioCommandType.valueOf((String) pCommand.get("type"));
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Szenario.class, new SzenarioAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();			
		SzenarioCommand command = localgson.fromJson(pCommand.toString(), SzenarioCommand.class);
		
		switch (type) {
			case Control:
				szenarioControl(command);
				break;
			case Synchron:
				// TODO
				break;
			case Follow:
				// TODO
				break;
			default:
				break;
		}
	}
	
	/**
	 * <h1>Parse control</h1>
	 * Parse of a szenario command with control.
	 * 
	 * @param pCommand Command as json object.
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private void szenarioControl(SzenarioCommand pCommand) throws Exception{
		ControlType type = ControlType.valueOf(pCommand.getSzenario().getSzenariotype());
		Client localclient = null;
		for(int i=0;i<Server.getClients().size();i++){
			if(pCommand.getSzenario().getRobots().get(0).getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
				localclient = Server.getClients().get(i);
				break;
			}
		}

		switch (type) {
			case Begin:
				SzenarioController.addNew(pCommand.getSzenario());
				RobotController.changeActive(pCommand.getSzenario().getRobots().get(0), true);
				AppController.changeActive(pCommand.getSzenario().getApps().get(0), true);
				
				//Set the szenario to the clients
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pCommand.getSzenario().getApps().size();j++){
						if(Server.getClients().get(i).getDevice() instanceof App){
							if(((App)Server.getClients().get(i).getDevice()).getIdentification().getId() == pCommand.getSzenario().getApps().get(j).getIdentification().getId())
								Server.getClients().get(i).setSzenario(pCommand.getSzenario());
						}
					}
				}
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pCommand.getSzenario().getRobots().size();j++){
						if(Server.getClients().get(i).getDevice() instanceof Robot){
							if(((Robot)Server.getClients().get(i).getDevice()).getIdentification().getId() == pCommand.getSzenario().getRobots().get(j).getIdentification().getId())
								Server.getClients().get(i).setSzenario(pCommand.getSzenario());
						}
					}
				}
				
				break;
			case End:
				SzenarioController.remove(pCommand.getSzenario());
				RobotController.changeActive(pCommand.getSzenario().getRobots().get(0), false);
				AppController.changeActive(pCommand.getSzenario().getApps().get(0), false);
				
				//Set the szenario to the clients of null
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pCommand.getSzenario().getApps().size();j++){
						if(Server.getClients().get(i).getDevice() instanceof App){
							if(((App)Server.getClients().get(i).getDevice()).getIdentification().getId() == pCommand.getSzenario().getApps().get(j).getIdentification().getId())
								Server.getClients().get(i).setSzenario(null);
						}
					}
				}
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pCommand.getSzenario().getRobots().size();j++){
						if(Server.getClients().get(i).getDevice() instanceof Robot){
							if(((Robot)Server.getClients().get(i).getDevice()).getIdentification().getId() == pCommand.getSzenario().getRobots().get(j).getIdentification().getId())
								Server.getClients().get(i).setSzenario(null);
						}
					}
				}
				break;
			case Start:
				//No implementation needed
				break;
			case Stop:
				//No implementation needed
				break;
			case Control:
				//No implementation needed
				break;
			default:
				throw new Exception("Argument out of range");
		}
		Control control = (Control) pCommand.getSzenario();
		Gson gson = new Gson();
		ControlCommand cmd = new ControlCommand(SzenarioCommandType.Control.toString(), control.getSzenariotype(), client.getIdentification(), pCommand.getSzenario().getRobots().get(0), control.getSteering());
		Server.sendCmd(localclient, gson.toJson(cmd));
	}
	
	private void exception(JSONObject pCommand) throws JSONException, IOException {
		// TODO
		/*if(pCommand == null) throw new NullPointerException();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();		
		
		ExceptionCommand command = localgson.fromJson(pCommand.toString(), ExceptionCommand.class);
		System.out.println(command.getException().getMessage());
		for(int i=0;i<Server.getClients().size();i++){
			if(Server.getClients().get(i).getIdentification().getId() == ((Robot)command.getException().getDevice()).getIdentification().getId()){
				Server.sendCmd(Server.getClients().get(i), command.getCommand());
				break;
			}
		}*/
	}
}
