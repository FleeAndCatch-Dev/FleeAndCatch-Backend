package flee_and_catch.backend.communication;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.util.Objects;

import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.Connection;
import flee_and_catch.backend.communication.command.connection.ConnectionType;

public class Interpreter {

	private Client client;
	private Gson gson;
	
	public Interpreter(Client pClient){
		this.client = pClient;
		this.gson = new Gson();
	}

	public void parse(String pCommand) throws Exception {
		JSONObject jsonCommand = new JSONObject(pCommand);
		
		if(!Objects.equals("@@fleeandcatch@@", (String) jsonCommand.get("apiid")))
			throw new Exception("Wrong apiid in json command");
		CommandType.Type id = CommandType.Type.valueOf((String) jsonCommand.get("id"));
		
		switch (id) {
			case Connection:
				connection(jsonCommand);
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}

	private void connection(JSONObject pCommand) throws Exception {
		if(pCommand == null) throw new NullPointerException();
		ConnectionType.Type type = ConnectionType.Type.valueOf((String) pCommand.get("type"));
		Connection command = gson.fromJson(pCommand.toString(), Connection.class);
		
		switch(type){
			case SetType:
				client.setType(Type.valueOf(command.getClient().getType()));
				System.out.println("Type of client: " + client.getId() + " set as " + client.getType().toString());
				return;
			case Disconnect:
				Connection cmd = new Connection(CommandType.Type.Connection.toString(), ConnectionType.Type.Disconnect.toString(), new flee_and_catch.backend.communication.command.connection.Client(client.getId()));
				Server.sendCmd(client, cmd.GetCommand());
				Server.removeClient(client);
				return;
			default:
				throw new Exception("Argument out of range");
		}
	}
}
