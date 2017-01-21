package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.NEW;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.ExceptionCommand;
import flee_and_catch.backend.communication.command.ExceptionCommandType;
import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;
import flee_and_catch.backend.communication.command.szenario.Szenario;
import flee_and_catch.backend.controller.AppController;
import flee_and_catch.backend.controller.RobotController;
import flee_and_catch.backend.controller.SzenarioController;


public final class Server {
	private static boolean opened;
	private static int port;
	private static ServerSocket serverSocket;
	private static ArrayList<Client> clients = new ArrayList<Client>();
	
	/**
	 * <h1>Open server</h1>
	 * Open the server at a default port.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public static void open() throws IOException{
		if(!opened){
			port = Default.port;
			serverSocket = new ServerSocket(port);
			Thread listenerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						listen();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			listenerThread.start();
			return;
		}
	}

	/**
	 * <h1>Open server</h1>
	 * Open server at a given port.
	 * 
	 * @param pPort Port as integer.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public static void open(int pPort) throws IOException{
		if(!opened){
			port = pPort;
			serverSocket = new ServerSocket(port);
			Thread listenerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						listen();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			listenerThread.start();
			return;
		}
	}
	
	/**
	 * <h1>Listen</h1>
	 * Listen on port for new clients. Open a new thread for every accepted client.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	private static void listen() throws IOException {
		opened = true;
		while(opened){
			System.out.println("Wait for clients ...");
			final Socket socket = serverSocket.accept();
			final int id = generateNewClientId();
			Thread clientThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						newClient(socket, id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if(e instanceof SocketException){
							try {
								handleException(id, e);
							} catch (IOException | JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else{
							e.printStackTrace();
						}
					}
				}
			});
			clientThread.start();
		}
	}

	/**
	 * <h1>New client</h1>
	 * Add new client and wait for new commands at the socket.
	 * 
	 * @param pSocket Current socket.
	 * @param pId Id of the client.
	 * 
	 * @throws Exception
	 * 
	 * @author ThunderSL94
	 */
	private static void newClient(Socket pSocket, int pId) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(pSocket.getOutputStream());
		
		Client client = new Client(true, new ClientIdentification(pId, pSocket.getInetAddress().getHostAddress(), port, IdentificationType.Undefined.toString()), pSocket, bufferedReader, outputStream);
		clients.add(client);
		
		while(client.isConnected()){
			client.getInterpreter().parse(receiveCmd(client));
		}
	}
	
	/**
	 * <h1>Send command</h1>
	 * Send json command to client.
	 * 
	 * @param pOutputStream Outputstream of client.
	 * @param pCommand Command as json string.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public static void sendCmd(OutputStream pOutputStream, String pCommand) throws IOException, JSONException {
		checkCmd(pCommand);
		
		byte[] size = new byte[4];
		int rest = pCommand.length();
		for(int i=0; i<size.length; i++){
			size[size.length - (i + 1)] = (byte) (rest / Math.pow(128, size.length - (i + 1)));
			rest = (int) (rest % Math.pow(128, size.length - (i + 1)));
		}
		
		pOutputStream.write(size);
		pOutputStream.flush();
		
		pOutputStream.write(pCommand.getBytes());
		pOutputStream.flush();
	}
	
	/**
	 * <h1>Send command</h1>
	 * Send json command to client.
	 * 
	 * @param pClient Client to send a command.
	 * @param pCommand Command as json string.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	public static void sendCmd(Client pClient, String pCommand) throws IOException, JSONException {
		checkCmd(pCommand);
		
		byte[] size = new byte[4];
		int rest = pCommand.length();
		for(int i=0; i<size.length; i++){
			size[size.length - (i + 1)] = (byte) (rest / Math.pow(128, size.length - (i + 1)));
			rest = (int) (rest % Math.pow(128, size.length - (i + 1)));
		}
		
		pClient.getOutputStream().write(size);
		pClient.getOutputStream().flush();
		
		pClient.getOutputStream().write(pCommand.getBytes());
		pClient.getOutputStream().flush();
	}
	
	/**
	 * <h1>Receive command</h1>
	 * Receive command from client.
	 * 
	 * @param pClient Client to receive a command.
	 * @return Command as json string.
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	private static String receiveCmd(Client pClient) throws IOException{
		char[] value = new char[4];
		int result = pClient.getBufferedReader().read(value);
		
		if(result >= 0) {
			int length = 0;
			for(int i = 0; i < value.length; i++) {
				length += (int) (value[i] * Math.pow(128, i));
			}
			
			value = new char[length];
			for( int i=0; i<value.length; i++) {
				char[] tmp = new char[1];
				pClient.getBufferedReader().read(tmp, 0, 1);
				value[i] = tmp[0];
			}
			
			return new String(value);
		}
		else {
			removeClient(pClient);
			return null;
		}
	}
	
	/**
	 * <h1>Remove client</h1>
	 * Remove the connection to a client.
	 * 
	 * @param pClient Client to remove.
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public static void removeClient(Client pClient) throws IOException{
		pClient.getBufferedReader().close();
		pClient.getOutputStream().close();
		pClient.setConnected(false);		
		pClient.getSocket().close();
		clients.remove(pClient);
	}

	/**
	 * <h1>Generate new id</h1>
	 * Generate new client id and sort the list.
	 * 
	 * @return id as integer.
	 * 
	 * @author ThunderSL94
	 */
	public static int generateNewClientId() {
		int result = 0;
		ArrayList<Client> tmpclients = clients;
		ArrayList<Client> sortclients = new ArrayList<Client>();
		
		for(int j=0; j<tmpclients.size(); j++){
			for(int i=0; i<tmpclients.size() - 1; i++){
				if(tmpclients.get(i).getIdentification().getId() < tmpclients.get(i + 1).getIdentification().getId()){
					continue;
				}
				sortclients.add(tmpclients.get(i));
				tmpclients.remove(i);				
			}
		}
		
		if(sortclients.size() > 0)
			clients = sortclients;	
		
		for( int i=0; i<clients.size(); i++) {
			if(clients.get(i).getIdentification().getId() == result)
				result++;
		}
		
		return result;
	}
	
	/**
	 * <h1>Check json</h1>
	 * Check string of correct json syntax.
	 * 
	 * @param pCommand Command as string.
	 * 
	 * @return parsed json object.
	 * 
	 * @throws JSONException
	 * 
	 * @author ThunderSL94
	 */
	private static JSONObject checkCmd(String pCommand) throws JSONException {
		return new JSONObject(pCommand);
	}

	private static void handleException(int pId, Exception e) throws IOException, JSONException{
		//Get the client of the id
		Client client = null;
		for(int i=0; i<clients.size(); i++){
			if(pId == clients.get(i).getIdentification().getId()){
				client = clients.get(i);
				break;
			}
		}
		
		if(client != null){
			//Is the current device in a szenario
			Szenario szenario = null;
			IdentificationType type = IdentificationType.valueOf(client.getIdentification().getType());
			for(int i=0; i<SzenarioController.getSzenarios().size(); i++){
				switch (type) {
					case App:
						App app = (App)client.getDevice();
						for(int j=0; j<SzenarioController.getSzenarios().get(i).getApps().size(); j++){
							if(app.getIdentification().getId() == SzenarioController.getSzenarios().get(i).getApps().get(j).getIdentification().getId()){
								szenario = SzenarioController.getSzenarios().get(i);
								break;
							}
						}
						break;
					case Robot:
						Robot robot = (Robot)client.getDevice();
						for(int j=0; j<SzenarioController.getSzenarios().get(i).getRobots().size(); j++){
							if(robot.getIdentification().getId() == SzenarioController.getSzenarios().get(i).getRobots().get(j).getIdentification().getId()){
								szenario = SzenarioController.getSzenarios().get(i);
								break;
							}
						}
						break;
					default:
						break;
				}
			}
			
			if(szenario != null){
				//The device is in a szenario
				ExceptionCommand cmd = new ExceptionCommand(CommandType.Exception.toString(), ExceptionCommandType.UnhandeldDisconnection.toString(), client.getIdentification(), new flee_and_catch.backend.communication.command.exception.Exception(ExceptionCommandType.UnhandeldDisconnection.toString(), e.getMessage(), client.getDevice()));
				for(int i=0; i<szenario.getApps().size(); i++){
					if(szenario.getApps().get(i).getIdentification().getId() != client.getIdentification().getId()){
						szenario.getApps().get(i).setActive(false);
						for(int j=0; j<clients.size(); j++){
							if(clients.get(j).getIdentification().getId() == szenario.getApps().get(i).getIdentification().getId()){
								Server.sendCmd(clients.get(j), cmd.getCommand());
							}
						}
					}
				}
				for(int i=0; i<szenario.getRobots().size(); i++){
					if(szenario.getRobots().get(i).getIdentification().getId() != client.getIdentification().getId()){
						szenario.getRobots().get(i).setActive(false);
						for(int j=0; j<clients.size(); j++){
							if(clients.get(j).getIdentification().getId() == szenario.getRobots().get(i).getIdentification().getId()){
								Server.sendCmd(clients.get(j), cmd.getCommand());
							}
						}
					}
				}
				SzenarioController.getSzenarios().remove(szenario);
			}
			
			//Remove device from controller
			switch (type) {
				case App:
					App app = (App)client.getDevice();
					AppController.getApps().remove(app);
					break;
				case Robot:
					Robot robot = (Robot)client.getDevice();
					RobotController.getRobots().remove(robot);
					break;
				default:
					break;
			}
			//Close client
			removeClient(client);
		}
	}
	
	
	
	/*Client client = null;
	for(int i=0; i<clients.size(); i++){
		if(id == clients.get(i).getIdentification().getId()){
			client = clients.get(i);
			break;
		}
	}
	
	if(client != null){
		ExceptionCommand cmd = new ExceptionCommand(CommandType.Exception.toString(), ExceptionCommandType.UnhandeldDisconnection.toString(), client.getIdentification(), new flee_and_catch.backend.communication.command.exception.Exception(ExceptionCommandType.UnhandeldDisconnection.toString(), e.getMessage(), client.getDevice()));
		IdentificationType type = IdentificationType.valueOf(client.getIdentification().getType());
		for(int i=0; i<SzenarioController.getSzenarios().size(); i++){
			switch (type) {
				case App:
					App app = (App)client.getDevice();
					for(int j=0; j<SzenarioController.getSzenarios().get(i).getApps().size(); j++){
						if(app.getIdentification().getId() == SzenarioController.getSzenarios().get(i).getApps().get(j).getIdentification().getId()){
							Szenario currentSzenario = SzenarioController.getSzenarios().get(i);
							for(int x=0; x<currentSzenario.getRobots().size(); x++){
								for(int y=0; y<clients.size(); y++){
									IdentificationType deviceType = IdentificationType.valueOf(clients.get(y).getIdentification().getType());
									if(deviceType == IdentificationType.Robot){
										Robot currentRobot = (Robot)clients.get(y).getDevice();
										if(currentRobot.getIdentification().getId() == currentSzenario.getRobots().get(x).getIdentification().getId()){
											currentSzenario.getRobots().get(x).setActive(false);
											try {
												Server.sendCmd(clients.get(y), cmd.getCommand());
											} catch (IOException | JSONException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
								}
							}											
							for(int x=0; x<currentSzenario.getApps().size(); x++){
								for(int y=0; y<clients.size(); y++){
									IdentificationType deviceType = IdentificationType.valueOf(clients.get(y).getIdentification().getType());
									if(deviceType == IdentificationType.App){
										App currentApp = (App)clients.get(y).getDevice();
										if(currentApp != app){
											if(currentApp.getIdentification().getId() == currentSzenario.getRobots().get(x).getIdentification().getId()){
												currentSzenario.getApps().get(x).setActive(false);
												try {
													Server.sendCmd(clients.get(y), cmd.getCommand());
												} catch (IOException | JSONException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
											}
										}
										else {
											currentApp.setActive(false);
										}	
									}													
								}
							}
							SzenarioController.getSzenarios().remove(currentSzenario);
						}
					}
					break;
				case Robot:
					for(int j=0; j<SzenarioController.getSzenarios().get(i).getRobots().size(); j++){
						
					}
					break;
				default:
					break;
			}
		}
	}*/
	
	public static boolean isOpened() {
		return opened;
	}

	public static ArrayList<Client> getClients() {
		return clients;
	}
}
