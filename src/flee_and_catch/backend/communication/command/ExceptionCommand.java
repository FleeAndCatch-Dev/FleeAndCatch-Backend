package flee_and_catch.backend.communication.command;

import flee_and_catch.backend.communication.command.identification.ClientIdentification;

public class ExceptionCommand extends Command {
	private flee_and_catch.backend.communication.command.exception.Exception exception;

	public ExceptionCommand(String pId, String pType, ClientIdentification pIdentification, flee_and_catch.backend.communication.command.exception.Exception pException) {
		super(pId, pType, pIdentification);
		this.exception = pException;
	}

	public flee_and_catch.backend.communication.command.exception.Exception getException() {
		return exception;
	}
}
