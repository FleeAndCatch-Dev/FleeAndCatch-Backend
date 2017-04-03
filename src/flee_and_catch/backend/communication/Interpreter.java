package flee_and_catch.backend.communication;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.ConnectionCommand;
import flee_and_catch.backend.communication.command.ConnectionCommandType;
import flee_and_catch.backend.communication.command.ControlCommand;
import flee_and_catch.backend.communication.command.ExceptionCommand;
import flee_and_catch.backend.communication.command.ExceptionCommandType;
import flee_and_catch.backend.communication.command.PositionCommand;
import flee_and_catch.backend.communication.command.PositionCommandType;
import flee_and_catch.backend.communication.command.SynchronizationCommand;
import flee_and_catch.backend.communication.command.SynchronizationCommandType;
import flee_and_catch.backend.communication.command.SzenarioCommand;
import flee_and_catch.backend.communication.command.SzenarioCommandType;
import flee_and_catch.backend.communication.command.device.Device;
import flee_and_catch.backend.communication.command.device.DeviceAdapter;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Position;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;
import flee_and_catch.backend.communication.command.exception.Exception;
import flee_and_catch.backend.communication.command.identification.IdentificationType;
import flee_and_catch.backend.communication.command.szenario.Control;
import flee_and_catch.backend.communication.command.szenario.ControlType;
import flee_and_catch.backend.communication.command.szenario.Follow;
import flee_and_catch.backend.communication.command.szenario.FollowType;
import flee_and_catch.backend.communication.command.szenario.Synchron;
import flee_and_catch.backend.communication.command.szenario.Szenario;
import flee_and_catch.backend.communication.command.szenario.SzenarioAdapter;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.controller.SzenarioController;
import flee_and_catch.backend.view.ViewController;

public class Interpreter {

	private Client client;
	private Gson gson;
	private long initClock;
	private boolean started;
	
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
	public void parse(String pCommand) {
		JSONObject jsonCommand = null;
		CommandType id = null;
		try {
			jsonCommand = new JSONObject(pCommand);
			if(!Objects.equals("@@fleeandcatch@@", (String) jsonCommand.get("apiid")))
				System.out.println("114 " + "Wrong apiid");
			id = CommandType.valueOf((String) jsonCommand.get("id"));
		} catch (JSONException e) {
			System.out.println("113 " + e.getMessage());
		} catch (NullPointerException e) {
			ViewController.printDebugLine("ERROR: " + e.getMessage() + " / " + e.getStackTrace().toString());
		}
		
		if(jsonCommand != null && id != null){
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
				System.out.println("115 " + "Wrong command type");
				return;
			}
		}
		System.out.println("116 " + "The json object doesn't exist");
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
	private void connection(JSONObject pCommand) {		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();
		
		ConnectionCommand command = localgson.fromJson(pCommand.toString(), ConnectionCommand.class);
		
		ConnectionCommandType type = ConnectionCommandType.valueOf(command.getType());		
		
		switch(type){
			case Connect:
				//Start connection of device
				ViewController.increaseNoOfConnectPackages();
				
				initClock = System.currentTimeMillis();
				
				command = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Init.toString(), client.getIdentification(), client.getDevice());
				Server.sendCmd(client, gson.toJson(command));
				return;
			case Disconnect:
				//Disconnect device
				ViewController.increaseNoOfDisconnectPackages();
				ConnectionCommand cmd = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Disconnect.toString(), client.getIdentification(), command.getDevice());				
				Server.sendCmd(client, gson.toJson(cmd));
				IdentificationType clienttype = IdentificationType.valueOf(command.getIdentification().getType());
				switch(clienttype){
				case App:
					//Remove app
					for(int i=0; i<AppController.getApps().size(); i++){
						if(AppController.getApps().get(i).getIdentification().getId() == command.getIdentification().getId()){
							//App app = new App(AppController.getApps().get(i));
							App app = AppController.getApps().get(i);
							AppController.remove(app);
							break;
						}
					}
					break;
				case Robot:			
					//Remove robot
					for(int i=0; i<RobotController.getRobots().size(); i++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getIdentification().getId()){
							//Robot robot = new Robot(RobotController.getRobots().get(i));
							Robot robot = RobotController.getRobots().get(i);
							RobotController.remove(robot);
							break;
						}
					}
					break;
				default:
					System.out.println("117 " + "Wrong identification type of device");
					break;
				}
				Server.removeClient(client);
				return;
			case Init:
				//Initialization of device
				long time = System.currentTimeMillis() - initClock;
				if(time <= 300){
					//Set id and update object instances
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
						System.out.println("117 " + "Wrong identification type of device");
						break;
					}	
					command = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Connect.toString(), client.getIdentification(), client.getDevice());
					Server.sendCmd(client, gson.toJson(command));
				}
				else {
					initClock = System.currentTimeMillis();
					
					//Send new init command
					command = new ConnectionCommand(CommandType.Connection.toString(), ConnectionCommandType.Init.toString(), client.getIdentification(), client.getDevice());
					Server.sendCmd(client, gson.toJson(command));
				}
				return;
			default:
				System.out.println("118 " + "Wrong connection type of json command");
				return;
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
	private void synchronization(JSONObject pCommand) {			
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();
		
		SynchronizationCommand command = localgson.fromJson(pCommand.toString(), SynchronizationCommand.class);
		SynchronizationCommandType type = SynchronizationCommandType.valueOf(command.getType());
		
		ViewController.increaseNoOfSyncPackages();
		
		switch (type) {
		case AllRobots:	
			if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
				SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.AllRobots.toString(), client.getIdentification(), new ArrayList<Szenario>(), RobotController.getRobots());
				Server.sendCmd(client, gson.toJson(cmd));
				return;
			}
			System.out.println("119 " + "A robot can not get a list of robots");
			return;
		case CurrentRobot:
			//New update from a robot
			if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.Robot){
				
				if(SzenarioController.getSzenarioOfDevice(command.getRobots().get(0).getIdentification().getId(), IdentificationType.valueOf(command.getRobots().get(0).getIdentification().getType())) != null){
					int deviceID = command.getIdentification().getId();
					Robot robotData = command.getRobots().get(0);
					ViewController.updateSensorData(deviceID, robotData);
					
					//Update the client of the robot
					client.setDevice(command.getRobots().get(0));
					
					//Update the robotController
					RobotController.update(command.getRobots().get(0));
					
					//Update the szenario of the robot
					Szenario szenario = SzenarioController.getSzenarioOfDevice(command.getIdentification().getId(), IdentificationType.valueOf(command.getIdentification().getType()));
					if(szenario != null){
						//Update the szenario
						for(int i=0;i<szenario.getRobots().size();i++){
							if(szenario.getRobots().get(i).getIdentification().getId() == command.getRobots().get(0).getIdentification().getId())
								szenario.getRobots().set(i, command.getRobots().get(0));
						}
						
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
								SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.CurrentRobot.toString(), client.getIdentification(), new ArrayList<Szenario>(), command.getRobots());
								Server.sendCmd(localclient, gson.toJson(cmd));
							}
						}
					}
				}
			}

			return;
		case AllSzenarios:
			//Send the app the update date of all szenarios
			if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
				SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.AllSzenarios.toString(), client.getIdentification(), SzenarioController.getSzenarios(), new ArrayList<Robot>());
				Server.sendCmd(client, gson.toJson(cmd));
				return;
			}
			System.out.println("120 " + "A robot can not get a list of szenarios");
			return;
		case CurrentSzenario:
			//Send the app the update data of the senario
			if(IdentificationType.valueOf(command.getIdentification().getType()) == IdentificationType.App){
				ArrayList<Szenario> szenarios = new ArrayList<Szenario>();
				Szenario szenario = null;
				for(int i=0;i<SzenarioController.getSzenarios().size();i++){
					if(SzenarioController.getSzenarios().get(i).getId() == command.getSzenarios().get(0).getId())
						szenario = SzenarioController.getSzenarios().get(i);
				}
				szenarios.add(szenario);
				SynchronizationCommand cmd = new SynchronizationCommand(CommandType.Synchronization.toString(), SynchronizationCommandType.CurrentSzenario.toString(), client.getIdentification(), szenarios, new ArrayList<Robot>());
				Server.sendCmd(client, gson.toJson(cmd));
				return;
			}
			System.out.println("121 " + "A robot can not get a szenario");
			return;
		default:
			System.out.println("122 " + "Wrong synchronization type of json command");
			return;
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
	private void szenario(JSONObject pCommand) {	
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Szenario.class, new SzenarioAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();
		
		SzenarioCommand command = localgson.fromJson(pCommand.toString(), SzenarioCommand.class);
		
		ViewController.increaseNoOfScenarioPackages();
		
		SzenarioCommandType type = SzenarioCommandType.valueOf(command.getType());
		
		switch (type) {
			case Init:
				boolean check = true;
				for(int i=0;i<RobotController.getRobots().size();i++){
					for(int j=0;j<command.getSzenario().getRobots().size();j++){
						if(RobotController.getRobots().get(i).getIdentification().getId() == command.getSzenario().getRobots().get(j).getIdentification().getId() && RobotController.getRobots().get(i).isActive()){
							check = false;
							break;
						}
					}
				}
				if(check){
					//Create of szenario is successful
					//Add the szenario and generate new id
					Szenario szenario = SzenarioController.addNew(command.getSzenario());
					
					SzenarioCommand cmd = new SzenarioCommand(CommandType.Szenario.toString(), SzenarioCommandType.Init.toString(), client.getIdentification(), szenario);
					Server.sendCmd(client, gson.toJson(cmd));
					started = true;
					return;	
				}
				//Create of szenario failed
				ExceptionCommand cmd = new ExceptionCommand(CommandType.Exception.toString(), ExceptionCommandType.CreateSzenario.toString(), client.getIdentification(), new Exception(ExceptionCommandType.CreateSzenario.toString(), "The szenario could not create", client.getDevice()));
				Server.sendCmd(client, gson.toJson(cmd));
				return;
			case End:
				started = false;
				SzenarioController.close(client, command.getSzenario());
			case Control:
				if(started)
					szenarioControl(command);
				return;
			case Synchron:
				if(started)
					szenarioSynchron(command);
				return;
			case Follow:
				if(started)
					szenarioFollow(command);
				return;
			default:
				System.out.println("123 " + "Wrong szenario type of json command");
				return;
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
	private void szenarioControl(SzenarioCommand pCommand) {
		ControlType type = ControlType.valueOf(pCommand.getSzenario().getCommand());
		
		ViewController.increaseNoOfControlPackages();
		
		//Get the client of the robot
		Client localclient = null;
		for(int i=0;i<Server.getClients().size();i++){
			if(pCommand.getSzenario().getRobots().get(0).getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
				localclient = Server.getClients().get(i);
				break;
			}
		}
		if(localclient != null){
			switch (type) {
			case Begin:				
				//Set devices active -> true
				for(int i=0;i<pCommand.getSzenario().getRobots().size();i++){
					RobotController.changeActive(pCommand.getSzenario().getRobots().get(i), true);
				}
				for(int i=0;i<pCommand.getSzenario().getApps().size();i++){
					AppController.changeActive(pCommand.getSzenario().getApps().get(i), true);
				}			
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
			case Start:
				//No implementation needed
				break;
			case Stop:
				//No implementation needed
				break;
			case Control:
				int deviceID = pCommand.getIdentification().getId();
				Control control = (Control) pCommand.getSzenario();
				Steering steeringCmd = control.getSteering();
				ViewController.updateControlData(deviceID, steeringCmd);
				//No implementation needed
				break;
			default:
				System.out.println("126 " + "Wrong control type of json command");
				return;
			}
			Control control = (Control) pCommand.getSzenario();
			ControlCommand cmd = new ControlCommand(CommandType.Control.toString(), control.getCommand(), client.getIdentification(), pCommand.getSzenario().getRobots().get(0), control.getSteering());
			Server.sendCmd(localclient, gson.toJson(cmd));
			return;
		}	
		System.out.println("125 " + "The client to send the command doesn't exist");
	}
	private void szenarioSynchron(SzenarioCommand pCommand) {
		
		ControlType type = ControlType.valueOf(pCommand.getSzenario().getCommand());
		
		//ViewController.increaseNoOfControlPackages();
		
		//Get the client of the robot
		ArrayList<Client> clients = new ArrayList<Client>();
		
		//Run through all involved robots of the scenario:
		for(Robot robot : pCommand.getSzenario().getRobots()) {
			//Run through all existing clients:
			for(Client client : Server.getClients()){
				//Check if client is involved:
				if(robot.getIdentification().getId() == client.getIdentification().getId()){
					clients.add(client);	//Add client to the necessary clients!
				}
			}
		}

		if(!clients.isEmpty()){
			
			switch (type) {
			
				case Begin:
					
					//Set devices active -> true
					for(int i=0;i<pCommand.getSzenario().getRobots().size();i++){
						RobotController.changeActive(pCommand.getSzenario().getRobots().get(i), true);
					}
					for(int i=0;i<pCommand.getSzenario().getApps().size();i++){
						AppController.changeActive(pCommand.getSzenario().getApps().get(i), true);
					}		
					
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
					System.out.println("126 " + "Wrong control type of json command");
					return;
			}
			
			//Send command to all involved clients:
			for(int i=0;i<clients.size();i++) {
				//Build command:
				Synchron synchron = (Synchron) pCommand.getSzenario();
				ControlCommand cmd = new ControlCommand(CommandType.Control.toString(), synchron.getCommand(), client.getIdentification(), pCommand.getSzenario().getRobots().get(i), synchron.getSteering());
				
				Server.sendCmd(clients.get(i), gson.toJson(cmd));
			}
			return;
		}	
		System.out.println("125 " + "The client to send the command doesn't exist");
	}
	
	private void szenarioFollow(SzenarioCommand pCommand) {
		
		FollowType type = FollowType.valueOf(pCommand.getSzenario().getCommand());
		
		//ViewController.increaseNoOfControlPackages();
		
		//Get the client of the robot
		ArrayList<Client> clients = new ArrayList<Client>();
		
		//Run through all involved robots of the scenario:
		for(Robot robot : pCommand.getSzenario().getRobots()) {
			//Run through all existing clients:
			for(Client client : Server.getClients()){
				//Check if client is involved:
				if(robot.getIdentification().getId() == client.getIdentification().getId()){
					clients.add(client);	//Add client to the necessary clients!
				}
			}
		}

		if(!clients.isEmpty()){
			
			switch (type) {
			
				case Begin:
					
					//Set devices active -> true
					for(int i=0;i<pCommand.getSzenario().getRobots().size();i++){
						RobotController.changeActive(pCommand.getSzenario().getRobots().get(i), true);
					}
					for(int i=0;i<pCommand.getSzenario().getApps().size();i++){
						AppController.changeActive(pCommand.getSzenario().getApps().get(i), true);
					}		
					
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
					
					//Set start position of the robots
					for(int i=0;i<pCommand.getSzenario().getRobots().size();i++){
						pCommand.getSzenario().getRobots().get(i).setPosition(new Position((i * 250) * (-1), 0, 0));
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
					System.out.println("new " + "Wrong follow type of json command");
					return;
			}
			
			//Build command:
			Follow follow = (Follow) pCommand.getSzenario();
			ControlCommand cmd = new ControlCommand(CommandType.Control.toString(), follow.getCommand(), client.getIdentification(), pCommand.getSzenario().getRobots().get(0), follow.getSteering());
			
			Server.sendCmd(clients.get(0), gson.toJson(cmd));
			
			Szenario tempSzenario = SzenarioController.getSzenarioById(follow.getId());
			//Send command to all involved clients:
			for(int i=1;i<clients.size();i++) {
				if(follow.getCommand().equals(FollowType.Control.toString()))
						follow.setCommand(PositionCommandType.Position.toString());
				//Build command:				
				PositionCommand command = new PositionCommand(CommandType.Position.toString(), follow.getCommand(), client.getIdentification(), pCommand.getSzenario().getRobots().get(i), tempSzenario.getRobots().get(i - 1).getPosition(), tempSzenario.getSteering());
				
				Server.sendCmd(clients.get(i), gson.toJson(command));
			}
			return;
		}	
		System.out.println("125 " + "The client to send the command doesn't exist");
	}
	
	private void exception(JSONObject pCommand) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Device.class, new DeviceAdapter());
		builder.setPrettyPrinting();
		Gson localgson = builder.create();
		
		ExceptionCommand command = localgson.fromJson(pCommand.toString(), ExceptionCommand.class);
		
		ExceptionCommandType type = ExceptionCommandType.valueOf(command.getType());
		switch (type) {
			case Undefined:
				System.out.println("127 " + command.getException().getMessage());
				break;
			case UnhandeldDisconnection:
				Server.handleDisconnection(command.getIdentification().getId(), "Device is disconnecting");
				break;
			default:
				System.out.println("128 " + "Wrong exception type of json command");
				return;
		}
	}
}
