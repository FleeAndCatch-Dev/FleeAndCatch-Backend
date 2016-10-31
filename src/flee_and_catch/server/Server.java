package flee_and_catch.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.xml.crypto.Data;

//import org.json.JSONObject;

public class Server {

	private ArrayList<Socket> sockets;
	private ArrayList<RunnableNewClient> newClientThreads;
	private ArrayList<DataOutputStream> outputStreams;
	private ServerSocket serverSocket;
	private RunnableListener listenThread;
	
	/**Constructor
	 * <br>Create an instance of the class Server, with the default port.
	 *  
	 * @throws Exception  
	 * @author ThunderSL94
	 */
	public Server() {
		try {
			sockets = new ArrayList<Socket>();
			newClientThreads = new ArrayList<RunnableNewClient>();
			outputStreams = new ArrayList<DataOutputStream>();
			
			serverSocket = new ServerSocket(Default.port);
		}
		catch(Exception ex) {		
			//Handle Exception
		}
	}
	
	/**Constructor
	 * <br>Create an instance of the class Server at a given port.
	 * 
	 * @param pPort Number of the port.
	 * @throws Exception  
	 * @author ThunderSL94
	 */
	public Server(int pPort) {
		try {
			sockets = new ArrayList<Socket>();
			newClientThreads = new ArrayList<RunnableNewClient>();
			outputStreams = new ArrayList<DataOutputStream>();
			
			serverSocket = new ServerSocket(pPort);
		}
		catch(Exception ex) {		
			//Handle Exception
		}
	}
	
	/**Function
	 * <br>Open the socket at a defined port and runs in a new thread.
	 * 
	 * @author ThunderSL94
	 */
	public void open() {
		listenThread = new RunnableListener(this);
		listenThread.start();
	}
	
	/**Function
	 * <br>Server wait for clients and opens for every connection a new thread for an individual communication.
	 * 
	 *  @throws IOException
	 *  @author ThunderSL94
	 */
	public void listen() {
		while(true) {
			try {				
				System.out.println("Wait for client ...");
				Socket socket = serverSocket.accept();
				sockets.add(socket);
				
				RunnableNewClient newClient = new RunnableNewClient(this, socket);
				newClientThreads.add(newClient);
				newClient.start();
			}
			catch(IOException ex) {
				//Handle Exception
			}
		}
	}
	
	/**Function
	 * <br>Add a new client an wait for a communication.
	 * 
	 * @param pSocket
	 * @author ThunderSL94
	 */
	public void addClient(Socket pSocket){
		String data;
		char[] value;
		try {
			System.out.println("Add new client " + pSocket.getInetAddress());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream(pSocket.getOutputStream());
			outputStreams.add(outputStream);
			while(true) {
				value = new char[4];
				reader.read(value);
				int length = 0;
				for(int i = 0; i < value.length; i++) {
					length += (int) (value[i] * Math.pow(2, (value.length - (i + 1))));
				}
				
				value = new char[length];
				int result = reader.read(value);
				
				data = new String(value);
				System.out.println(value);
			}
		}
		catch(IOException ex) {
			//Handle Exception
		}		
	}
	
	/**Function
	 * <br>Remove the current client and close the session.
	 * 
	 * @param pid Id of the current client.
	 * @author ThunderSL94
	 */
	public void removeClient(int pid) {
		try {			
			System.out.println("Client is disconnected " + sockets.get(pid).getInetAddress());
			
			outputStreams.get(pid).close();
			outputStreams.remove(pid);
			
			sockets.get(pid).close();
			sockets.remove(pid);
		}
		catch(Exception ex) {
			//Handle Exception
		}
	}
	
	/**Function
	 * <br>Send a command with two sockets. The first socket sends the length as an integer of the data, the second contains the data.
	 * 
	 * @param pid The current id of the client.
	 * @param pCommand The json command to send as string.
	 * @author ThunderSL94
	 */
	public void sendCommand(int pid, String pCommand) {
		try {
			outputStreams.get(pid).writeInt(pCommand.length());
			outputStreams.get(pid).flush();
			outputStreams.get(pid).writeBytes(pCommand);
			outputStreams.get(pid).flush();
		}
		catch (IOException e) {
			// TODO: handle exception
		}
	}
}
