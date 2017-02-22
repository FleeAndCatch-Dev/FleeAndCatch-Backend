package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

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
import flee_and_catch.backend.view.Status;
import flee_and_catch.backend.view.ViewController;


public final class Server {
	private static boolean opened;
	private static int port;
	private static ServerSocket serverSocket;
	private static ArrayList<Client> clients = new ArrayList<Client>();
	private static Lock clientsLock = new ReentrantLock();
	
	/* Method that identifies the IP addresses of the maschine 
       - Must be not LoopBack!
       - Must be UP!
       - Must have MAC Address (is not null)
	 */
	private static String[] getHostAddresses() {
		  Set<String> HostAddresses = new HashSet<>();
		  try {
		    for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
		      if (!ni.isLoopback() && ni.isUp() && ni.getHardwareAddress() != null) {
		        for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
		          if (ia.getBroadcast() != null) {  //If limited to IPV4
		            HostAddresses.add(ia.getAddress().getHostAddress());
		          }
		        }
		      }
		    }
		  } catch (SocketException e) { }
		  return HostAddresses.toArray(new String[0]);
		}
	
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
			//Set backend infos:
			
			ViewController.setBackendIPAddress(Server.getHostAddresses()[0]);
			ViewController.setBackendPort(String.valueOf(serverSocket.getLocalPort()));
			
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
			ViewController.setStatus(Status.Waiting);;
			
			final Socket socket = serverSocket.accept();
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			
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
								handleDisconnection(id, e.getMessage());
							} catch (Exception e1) {
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
		
		//Set status message:
		ViewController.setStatus(Status.Connected);;
		int index = pSocket.getRemoteSocketAddress().toString().indexOf(":");
		
		Client client = new Client(true, new ClientIdentification(pId, pSocket.getRemoteSocketAddress().toString().substring(1, index), pSocket.getPort(), IdentificationType.Undefined.toString()), pSocket, bufferedReader, outputStream);
		ArrayList<Client> clientList = new ArrayList<Client>(getClients());
		clientList.add(client);
		
		//Set number of devices in view:
		ViewController.setNumberOfDevices(clientList.size());
		
		setClients(clientList);
		
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
		
		ArrayList<Client> clientList = new ArrayList<Client>(getClients());
		clientList.remove(pClient);
		//Set status message 
		ViewController.setStatus(Status.Disconnected);;
		//Set number of devices in view:
		ViewController.setNumberOfDevices(clientList.size());
		setClients(clientList);
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
		clientsLock.lock();
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
		
		if(sortclients.size() > 1)
			clients = sortclients;
		
		for( int i=0; i<getClients().size(); i++) {
			if(clients.get(i).getIdentification().getId() == result)
				result++;
		}
		clientsLock.unlock();
		
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

	public static Client getClientOfId(int pId){
		//Get the client of the id
		Client client = null;
		for(int i=0; i<getClients().size(); i++){
			if(pId == getClients().get(i).getIdentification().getId()){
				client = getClients().get(i);
				break;
			}	
		}
		return client;
	}	
	
	public static Szenario getSzenarioOfClient(Client pClient){
		//Is the current device in a szenario
		Szenario szenario = null;
		IdentificationType type = IdentificationType.valueOf(pClient.getIdentification().getType());
		for(int i=0; i<SzenarioController.getSzenarios().size(); i++){
			switch (type) {
				case App:
					App app = (App)pClient.getDevice();
					for(int j=0; j<SzenarioController.getSzenarios().get(i).getApps().size(); j++){
						if(app.getIdentification().getId() == SzenarioController.getSzenarios().get(i).getApps().get(j).getIdentification().getId()){
							szenario = SzenarioController.getSzenarios().get(i);
							break;
						}
					}
					break;
				case Robot:
					Robot robot = (Robot)pClient.getDevice();
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
		return szenario;
	}
	
	public static ArrayList<Client> getSzenarioMember(Szenario pSzenario){
		ArrayList<Client> szenarioMember = new ArrayList<Client>();
		
		for(int i=0; i<pSzenario.getApps().size(); i++){
			pSzenario.getApps().get(i).setActive(false);
			for(int j=0; j<getClients().size(); j++){
				if(getClients().get(j).getIdentification().getId() == pSzenario.getApps().get(i).getIdentification().getId()){
					szenarioMember.add(getClients().get(j));
				}
			}
		}
		for(int i=0; i<pSzenario.getRobots().size(); i++){
			pSzenario.getRobots().get(i).setActive(false);
			for(int j=0; j<getClients().size(); j++){
				if(getClients().get(j).getIdentification().getId() == pSzenario.getRobots().get(i).getIdentification().getId()){
					szenarioMember.add(getClients().get(j));
				}
			}
		}
		
		return szenarioMember;
	}
	
	public static void handleDisconnection(int pId, String pMessage) throws Exception{
		
		Client client = getClientOfId(pId);
		if(client != null){
			ExceptionCommand cmd = new ExceptionCommand(CommandType.Exception.toString(), ExceptionCommandType.UnhandeldDisconnection.toString(), client.getIdentification(), new flee_and_catch.backend.communication.command.exception.Exception(ExceptionCommandType.UnhandeldDisconnection.toString(), pMessage, client.getDevice()));
			Szenario szenario = getSzenarioOfClient(client);
			if(szenario != null){
				ArrayList<Client> szenarioMember = getSzenarioMember(szenario);
				
				for(int i=0; i<szenarioMember.size(); i++){
					Gson gson = new Gson();
					Server.sendCmd(szenarioMember.get(i), gson.toJson(cmd));
				}
				
				
				//Remove from szenariolist
				ArrayList<Szenario> szenarioList = new ArrayList<Szenario>(SzenarioController.getSzenarios());
				szenarioList.remove(szenario);
				SzenarioController.setSzenarios(szenarioList);
			}
			IdentificationType type = IdentificationType.valueOf(client.getIdentification().getType());
			//Remove device from controller
			switch (type) {
				case App:
					App app = (App)client.getDevice();
					AppController.remove(app);
					break;
				case Robot:
					Robot robot = (Robot)client.getDevice();
					RobotController.remove(robot);
					break;
				default:
					break;
			}
			//Close client
			removeClient(client);
			
			return;
		}
		throw new Exception("The disconnected device don't exist");
	}
	
	public static boolean isOpened() {
		return opened;
	}

	public static ArrayList<Client> getClients() {
		clientsLock.lock();
		ArrayList<Client> clientList = new ArrayList<Client>(clients);
		clientsLock.unlock();
		
		return clientList;
	}

	public static void setClients(ArrayList<Client> clients) {
		clientsLock.lock();
		Server.clients = clients;
		clientsLock.unlock();
	}
	
}
