package flee_and_catch.backend.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import flee_and_catch.backend.exception.OpenConnection;
import flee_and_catch.backend.exception.ParseCommand;

public class Server {
	
	private ServerSocket serverSocket;
	private boolean opened;
	private ArrayList<Client> clients;
	
	/**
	 * <h1>Constructor</h1>
	 * Creates an object of the class server.
	 * 
	 * @throws IOException
	 * 
	 * @author ThunderSL94
	 */
	public Server() throws IOException {
		this.serverSocket = new ServerSocket(Default.port);
		this.clients = new ArrayList<Client>();
		this.opened = false;
	}
	
	/**
	 * 
	 * @param pPort
	 * @throws IOException
	 */
	public Server(int pPort) throws IOException {
		this.serverSocket = new ServerSocket(pPort);
		this.clients = new ArrayList<Client>();
		this.opened = false;
	}
	
	/**
	 * 
	 * @throws OpenConnection
	 */
	public void open() throws OpenConnection{
		if(!opened){
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
		throw new OpenConnection();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void listen() throws IOException{
		opened = true;
		while(opened){
			System.out.println("Wait for clients ...");
			final Socket socket = serverSocket.accept();
			Thread clientThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						newClient(socket, generateNewClientId());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseCommand e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			clientThread.start();
		}
	}
	
	/**
	 * 
	 * @param pSocket
	 * @param pId
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParseCommand
	 */
	private void newClient(Socket pSocket, int pId) throws IOException, ParseException, ParseCommand{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(pSocket.getOutputStream());
		
		String jsonString = "{\"id\":\"Client\",\"type\":\"SetId\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCmd(outputStream, jsonString);
		jsonString = "{\"id\":\"Client\",\"type\":\"GetType\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCmd(outputStream, jsonString);
		
		Interpreter interpreter = new Interpreter(this);
		
		ClientType result = ClientType.valueOf(interpreter.interpret(receiveCmd(bufferedReader, pId)));;		
		Client client = new Client(pId, result, true, pSocket, bufferedReader, outputStream, interpreter);
		clients.add(client);
		
		System.out.println("New client added with id:" + String.valueOf(pId) + " and type: " + result.toString());
		
		while(client.isConnected()){
			String currentCmd = receiveCmd(bufferedReader, pId);
			interpreter.interpret(currentCmd);
		}
	}
	
	/**
	 * 
	 * @param pOutputStream
	 * @param pCommand
	 * @throws IOException
	 * @throws ParseException
	 */
	public void sendCmd(OutputStream pOutputStream, String pCommand) throws IOException, ParseException{
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
	 * 
	 * @param pOutputStream
	 * @param pCommand
	 * @throws IOException
	 * @throws ParseException
	 */
	public void sendCmd(Client pClient, String pCommand) throws IOException, ParseException{
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
	 * 
	 * @param pBufferedReader
	 * @param pId
	 * @return
	 * @throws IOException
	 */
	private String receiveCmd(BufferedReader pBufferedReader, int pId) throws IOException{
		char[] value = new char[4];
		int result = pBufferedReader.read(value);
		
		if(result >= 0) {
			int length = 0;
			for(int i = 0; i < value.length; i++) {
				length += (int) (value[i] * Math.pow(128, i));
			}
			
			value = new char[length];
			for( int i=0; i<value.length; i++) {
				char[] tmp = new char[1];
				pBufferedReader.read(tmp, 0, 1);
				value[i] = tmp[0];
			}
			
			return new String(value);
		}
		else {
			removeClient(clients.get(pId));
			return null;
		}
	}
	
	/**
	 * 
	 * @param pClient
	 * @throws IOException
	 */
	public void removeClient(Client pClient) throws IOException{
		pClient.getBufferedReader().close();
		pClient.getOutputStream().close();
		pClient.setConnected(false);		
		pClient.getSocket().close();
		clients.remove(pClient);
		
		System.out.println("Client with id " + pClient.getId() + " and type: " + pClient.getType().toString() + " disconnected");
	}
	
	/**
	 * 
	 * @return
	 */
	private int generateNewClientId(){
		int result = 0;
		
		for( int i=0; i<clients.size(); i++) {
			if(clients.get(i).getId() > result && (clients.get(i + 1).getId()) == (result + 1))
				result++;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param pCommand
	 * @throws ParseException
	 */
	private void checkCmd(String pCommand) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		jsonParser.parse(pCommand);
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException{
		for(int i=0; i<clients.size(); i++)
			removeClient(clients.get(i));
		this.opened = false;
		this.serverSocket.close();
		System.out.println("Server is closing ...");
	}

	public boolean isOpened() {
		return opened;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}
	
}
