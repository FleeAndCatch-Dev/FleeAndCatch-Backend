package flee_and_catch.backend.communication;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.ConnectionCommand;
import flee_and_catch.backend.communication.command.ConnectionCommandType;
import flee_and_catch.backend.communication.command.ControlCommand;
import flee_and_catch.backend.communication.command.ExceptionCommand;
import flee_and_catch.backend.communication.command.ExceptionCommandType;
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
					ArrayList<App> appList = new ArrayList<App>(AppController.getApps());
					appList.add(app);
					AppController.setApps(appList);
					break;
				case Robot:
					Robot robot = (Robot)command.getDevice();
					robot.getIdentification().setId(client.getIdentification().getId());
					command.getIdentification().setId(client.getIdentification().getId());
					client.setIdentification(command.getIdentification());
					client.setDevice(robot);
					ArrayList<Robot> robotList = new ArrayList<Robot>(RobotController.getRobots());
					robotList.add(robot);
					RobotController.setRobots(robotList);
					break;
				default:
					throw new Exception("Undefined connection");
				}	
				command = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Connect.toString(), client.getIdentification(), client.getDevice());
				Server.sendCmd(client, command.getCommand());
				return;
			case Disconnect:
				////////////Testing for bugs
				ConnectionCommand cmd = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Disconnect.toString(), client.getIdentification(), command.getDevice());
				
				Server.sendCmd(client, cmd.getCommand());
				/*clienttype = IdentificationType.valueOf(command.getIdentification().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getIdentification().getId() == command.getIdentification().getId()){
							App app = new App(AppController.getApps().get(i));
							ArrayList<App> appList = new ArrayList<App>(AppController.getApps());
							appList.remove(app);
							AppController.setApps(appList);
							break;
						}
					}
					break;
				case Robot:
					//Get the robot
					Robot robot = null;
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getIdentification().getId()){
							robot = new Robot(RobotController.getRobots().get(i));
							break;
						}
					}
					
					if(robot != null){
						//Is the robot in a szenario
						Szenario szenario = null;
						for(int i=0; i<SzenarioController.getSzenarios().size(); i++){
							for(int j=0; j<SzenarioController.getSzenarios().get(i).getRobots().size(); j++){
								if(SzenarioController.getSzenarios().get(i).getRobots().get(j).getIdentification().getId() == robot.getIdentification().getId()){
									SzenarioCommandType type2 = SzenarioCommandType.valueOf(SzenarioController.getSzenarios().get(i).getSzenarioid());
									switch (type2) {
										case Control:
											szenario = new Control((Control) SzenarioController.getSzenarios().get(i));
											break;
										default:
											break;
									}
								}
							}
						}
						
						if(szenario != null){
							//The device is in a szenario
							ExceptionCommand cmd1 = new ExceptionCommand(CommandType.Exception.toString(), ExceptionCommandType.Disconnection.toString(), client.getIdentification(), new flee_and_catch.backend.communication.command.exception.Exception(ExceptionCommandType.Disconnection.toString(), "Robot " + robot.getIdentification().getId() + " disconnect", robot));
							for(int i=0; i<szenario.getApps().size(); i++){
								szenario.getApps().get(i).setActive(false);
								for(int j=0; j<Server.getClients().size(); j++){
									if(Server.getClients().get(j).getIdentification().getId() == szenario.getApps().get(i).getIdentification().getId()){
										Server.sendCmd(Server.getClients().get(j), cmd1.getCommand());
									}
								}
							}
						}
						
						//Remove robot from list
						ArrayList<Robot> robotList = new ArrayList<Robot>(RobotController.getRobots());
						robotList.remove(robot);
						RobotController.setRobots(robotList);
						
						if(szenario != null && szenario.getRobots().size() == 0){
							//Remove from szenariolist
							ArrayList<Szenario> szenarioList = new ArrayList<Szenario>(SzenarioController.getSzenarios());
							szenarioList.remove(szenario);
							SzenarioController.setSzenarios(szenarioList);
						}
					}
					break;
				default:
					throw new Exception("Undefined disconnection");
				}
				Server.removeClient(client);*/
				
				Server.handleDisconnection(client.getIdentification().getId(), "Device is correct disconnecting");
				
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
					SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.All.toString(), client.getIdentification(), RobotController.getRobots());
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
					SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.All.toString(), client.getIdentification(), robotlist);
					Server.sendCmd(client, cmd.getCommand());
				}
				else if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.Robot) {
					for (int i = 0; i < RobotController.getRobots().size(); i++) {
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getRobots().get(0).getIdentification().getId()){
							ArrayList<Robot> robotList = new ArrayList<Robot>(RobotController.getRobots());
							robotList.set(i, command.getRobots().get(0));
							RobotController.setRobots(robotList);
						}
					}
				}
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
				break;
			case Follow:
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
				SzenarioController.getSzenarios().add(pCommand.getSzenario());
				RobotController.changeActive(pCommand.getSzenario().getRobots().get(0), true);
				break;
			case End:
				SzenarioController.getSzenarios().remove(pCommand.getSzenario());
				RobotController.changeActive(pCommand.getSzenario().getRobots().get(0), false);
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
		Control control = (Control) pCommand.getSzenario();
		ControlCommand cmd = new ControlCommand(SzenarioCommandType.Control.toString(), control.getSzenariotype(), client.getIdentification(), pCommand.getSzenario().getRobots().get(0), control.getSteering());
		Server.sendCmd(localclient, cmd.getCommand());
	}
}
