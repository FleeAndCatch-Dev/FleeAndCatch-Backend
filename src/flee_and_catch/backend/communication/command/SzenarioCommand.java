package flee_and_catch.backend.communication.command;

import flee_and_catch.backend.communication.command.identification.ClientIdentification;
import flee_and_catch.backend.communication.command.szenario.Szenario;

public class SzenarioCommand extends Command {
	private Szenario szenario;

	public SzenarioCommand(String pId, String pType, ClientIdentification pIdentification, Szenario pSzenario) {
		super(pId, pType, pIdentification);
		this.szenario = pSzenario;
	}

	public Szenario getSzenario() {
		return szenario;
	}
}
