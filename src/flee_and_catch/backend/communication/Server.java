package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.connection.Connection;
import flee_and_catch.backend.communication.command.connection.ConnectionType;

public final class Server {
	private static ServerSocket serverSocket;
	private static boolean opened;
	private static ArrayList<Client> clients = new ArrayList<Client>();
	
	public static void open() throws IOException{
		if(!opened){
			serverSocket = new ServerSocket(Default.port);
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

	public static void open(int pPort) throws IOException{
		if(!opened){
			serverSocket = new ServerSocket(pPort);
			serverSocket = new ServerSocket(Default.port);
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
	
	private static void listen() throws IOException {
		opened = true;
		while(opened){
			System.out.println("Wait for clients ...");
			final Socket socket = serverSocket.accept();
			Thread clientThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						newClient(socket, generateNewClientId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			clientThread.start();
		}
	}

	private static void newClient(Socket pSocket, int pId) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(pSocket.getOutputStream());
		
		Connection command = new Connection(CommandType.Type.Connection.toString(), ConnectionType.Type.SetId.toString(), new flee_and_catch.backend.communication.command.connection.Client(pId, "null", "null"));
		sendCmd(outputStream, command.GetCommand());
		command = new Connection(CommandType.Type.Connection.toString(), ConnectionType.Type.GetType.toString(), new flee_and_catch.backend.communication.command.connection.Client(pId, "null", "null"));
		sendCmd(outputStream, command.GetCommand());
		
		Client client = new Client(pId, true, pSocket, bufferedReader, outputStream);
		clients.add(client);
		
		System.out.println("New client added with id:" + String.valueOf(pId));
		
		while(client.isConnected()){
			client.getInterpreter().parse(receiveCmd(client));
		}
	}
	
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
	
	public static void removeClient(Client pClient) throws IOException{
		pClient.getBufferedReader().close();
		pClient.getOutputStream().close();
		pClient.setConnected(false);		
		pClient.getSocket().close();
		clients.remove(pClient);
	}

	private static int generateNewClientId() {
		int result = 0;
		ArrayList<Client> tmpclients = clients;
		ArrayList<Client> sortclients = new ArrayList<Client>();
		
		for(int j=0; j<tmpclients.size(); j++){
			for(int i=0; i<tmpclients.size() - 1; i++){
				if(tmpclients.get(i).getId() < tmpclients.get(i + 1).getId()){
					continue;
				}
				sortclients.add(tmpclients.get(i));
				tmpclients.remove(i);				
			}
		}
		
		if(sortclients.size() > 0)
			clients = sortclients;	
		
		for( int i=0; i<clients.size(); i++) {
			if(clients.get(i).getId() == result)
				result++;
		}
		
		return result;
	}
	
	private static JSONObject checkCmd(String pCommand) throws JSONException {
		return new JSONObject(pCommand);
	}

	public static boolean isOpened() {
		return opened;
	}

	public static ArrayList<Client> getClients() {
		return clients;
	}
}
