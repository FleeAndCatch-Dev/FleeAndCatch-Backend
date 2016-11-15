package flee_and_catch.backend.communication;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.util.Objects;

public class Server {

	private ServerSocket serverSocket;
	private boolean opened;
	private Thread listenerThread;
	private ArrayList<Client> clients;
	
	/**
	 * <h1>Constructor</h1>
	 * <br>Create an object of the class server, with the default port.
	 *  
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public Server() throws IOException{
		initComponents();
		serverSocket = new ServerSocket(Default.port);
	}
	
	/**
	 * <h1>Constructor</h1>
	 * <br>Create an object of the class server, with the given port.
	 * 
	 * @param pPort Number of the port.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public Server(int pPort) throws IOException{
		initComponents();
		serverSocket = new ServerSocket(pPort);
	}
	
	/**
	 * <h1>InitComponents</h1>
	 * Initialize all components which used in this object.
	 * 
	 * @author ThunderSL94
	 */
	private void initComponents() {
		opened = false;
		clients = new ArrayList<Client>();
	}
	
	/**
	 * <h1>Open socket</h1>
	 * Open the socket at a defined port and runs in a new thread.
	 * 
	 * @author ThunderSL94
	 */
	public void open() {
		if(!opened) {
			opened = true;
			listenerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						listen();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			listenerThread.start();
		}
		else {
			//Handle Exception
		}
	}
	
	/**
	 * <h1>Listen socket</h1>
	 * Server wait for clients and opens for every connection a new thread for an individual communication.
	 * 
	 *  @throws IOException
	 *  
	 *  @author ThunderSL94
	 */
	private void listen() throws IOException {	
		while(opened) {			
			System.out.println("Wait for client ...");
			
			Socket socket = serverSocket.accept();
			
			final int id = getNewId();
			
			Thread clientThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						addClient(id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			Client client = new Client(id, true, socket, clientThread);
			clients.add(client);
			
			clientThread.start();
		}
	}
	
	/**
	 * <h1>Add new client</h1>
	 * Add a new client an wait for a incomming communication.
	 * 
	 * @param pSocket Socket of the current client;
	 * 
	 * @throws InterruptedException 
	 * 
	 * @author ThunderSL94
	 * @throws ParseException 
	 */
	private void addClient(int pId) throws IOException, InterruptedException{
		String dataString = null;
		char[] value = null;
		
		System.out.println("Add new client " + clients.get(pId).getSocket().getInetAddress());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(clients.get(pId).getSocket().getInputStream()));
		clients.get(pId).setReader(reader);
		DataOutputStream outputStream = new DataOutputStream(clients.get(pId).getSocket().getOutputStream());
		clients.get(pId).setOutputStream(outputStream);
		
		clients.get(pId).setInterpreter(new Interpreter(clients.get(pId)));
		
		String jsonString = "{\"id\":\"Connection\",\"type\":\"SetId\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCommand(clients.get(pId), jsonString);
		jsonString = "{\"id\":\"Connection\",\"type\":\"GetType\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCommand(clients.get(pId), jsonString);

		while(clients.get(pId).isOpened()) {
			value = new char[4];
			int result = clients.get(pId).getReader().read(value);
			
			if(result >= 0) {
				int length = 0;
				for(int i = 0; i < value.length; i++) {
					length += (int) (value[i] * Math.pow(256, i));
				}
				
				value = new char[length];
				for( int i=0; i<value.length; i++) {
					char[] tmp = new char[1];
					clients.get(pId).getReader().read(tmp, 0, 1);
					value[i] = tmp[0];
				}
				
				String command = new String(value);
				clients.get(pId).getCommandList().add(command);
			}
			else {
				removeClient(clients.get(pId));
			}
		}
	}
	
	/**
	 * <h1>Send command</h1>
	 * Send a command with two sockets. The first socket sends the length as an integer of the data, the second contains the data.
	 * 
	 * @param pid The current id of the client.
	 * @param pCommand The json command to send as string.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public void sendCommand(Client pClient, String pCommand) throws IOException {
		byte[] size = new byte[4];
		int rest = pCommand.length();
		for(int i=0; i<size.length; i++){
			size[size.length - (i + 1)] = (byte) (rest / Math.pow(256, size.length - (i + 1)));
			rest = (int) (rest % Math.pow(256, size.length - (i + 1)));
		}
		
		pClient.getOutputStream().write(size);
		pClient.getOutputStream().flush();
		
		pClient.getOutputStream().writeBytes(pCommand);
		pClient.getOutputStream().flush();	
	}
	
	/**
	 * <h1>Remove client</h1>
	 * Remove the current client and close the session.
	 * 
	 * @param pid Id of the current client.
	 * 
	 * @throws InterruptedException 
	 * 
	 * @author ThunderSL94
	 */
	public void removeClient(Client pClient) throws IOException, InterruptedException {
		pClient.getReader().close();
		pClient.getOutputStream().close();
		pClient.setOpened(false);
		
		System.out.println("Client is disconnected " + pClient.getSocket().getInetAddress());
		pClient.getSocket().close();
		clients.remove(pClient);
	}
	
	/**
	 * <h1>Close server</h1>
	 * Remove all current clients and close the server.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * 
	 * @author ThunderSL94
	 */
	public void close() throws IOException, InterruptedException{
		for( int i=0; i<clients.size(); i++) {
			removeClient(clients.get(i));
		}
		
		this.opened = false;
		
		while(this.listenerThread.isAlive()){
			this.wait(5);
		}
		System.out.println("Close server ...");
		this.serverSocket.close();		
	}
	
	/**
	 * <h1>New id</h1>
	 * Calculate new id in reference of the exist clients.=hs
	 * 
	 * @return New id
	 * 
	 * @author ThunderSL94
	 */
	private int getNewId() {
		int result = 0;
		
		for( int i=0; i<clients.size(); i++) {
			if(clients.get(i).getId() > result && (clients.get(i + 1).getId()) == (result + 1))
				result++;
		}
		
		return result;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public Thread getListenerThread() {
		return listenerThread;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}
}
