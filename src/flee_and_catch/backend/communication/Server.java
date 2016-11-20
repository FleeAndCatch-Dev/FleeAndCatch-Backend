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
	
	public Server() throws IOException {
		this.serverSocket = new ServerSocket(Default.port);
		this.clients = new ArrayList<Client>();
		this.opened = false;
	}
	
	public Server(int pPort) throws IOException {
		this.serverSocket = new ServerSocket(pPort);
		this.clients = new ArrayList<Client>();
		this.opened = false;
	}
	
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
	
	private void newClient(Socket pSocket, int pId) throws IOException, ParseException, ParseCommand{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
		DataOutputStream outputStream = new DataOutputStream(pSocket.getOutputStream());
		
		String jsonString = "{\"id\":\"Client\",\"type\":\"SetId\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCmd(outputStream, jsonString);
		jsonString = "{\"id\":\"Client\",\"type\":\"GetType\",\"apiid\":\"@@fleeandcatch@@\",\"errorhandling\":\"ignoreerrors\",\"client\":{\"id\":" + String.valueOf(pId) + "}}";
		sendCmd(outputStream, jsonString);
		
		Interpreter interpreter = new Interpreter();
		
		ClientType result = ClientType.valueOf(interpreter.interpret(receiveCmd(bufferedReader, pId)));;		
		Client client = new Client(pId, result, true, pSocket, bufferedReader, outputStream, interpreter);
		
		while(client.isConnected()){
			String currentCmd = receiveCmd(bufferedReader, pId);
			interpreter.interpret(currentCmd);
		}
	}
	
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
	
	public void removeClient(Client pClient) throws IOException{
		pClient.getBufferedReader().close();
		pClient.getOutputStream().close();
		pClient.setConnected(false);		
		pClient.getSocket().close();
		clients.remove(pClient);
		
		System.out.println("Client with id " + pClient.getId() + " disconnected");
	}
	
	private int generateNewClientId(){
		int result = 0;
		
		for( int i=0; i<clients.size(); i++) {
			if(clients.get(i).getId() > result && (clients.get(i + 1).getId()) == (result + 1))
				result++;
		}
		
		return result;
	}
	
	private void checkCmd(String pCommand) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		jsonParser.parse(pCommand);
	}
	
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
}
