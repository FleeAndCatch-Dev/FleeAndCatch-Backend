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
import flee_and_catch.backend.communication.command.component.IdentificationType;
import flee_and_catch.backend.communication.command.identification.ClientIdentification;


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
		
		//client.getInterpreter().parse(receiveCmd(bufferedReader));
		
		/*Connection command = new Connection(CommandType.Connection.toString(), ConnectionType.SetId.toString(), new ClientIdentification(pId, pSocket.getInetAddress().getHostAddress(), port, IdentificationType.Undefined.toString()), null);
		sendCmd(outputStream, command.getCommand());
		command = new Connection(CommandType.Connection.toString(), ConnectionType.GetType.toString(), new ClientIdentification(pId, pSocket.getInetAddress().getHostAddress(), port, IdentificationType.Undefined.toString()), null);
		sendCmd(outputStream, command.getCommand());*/
		
		
		
		//System.out.println("New client added with id:" + String.valueOf(pId));
		
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

	public static boolean isOpened() {
		return opened;
	}

	public static ArrayList<Client> getClients() {
		return clients;
	}
}
