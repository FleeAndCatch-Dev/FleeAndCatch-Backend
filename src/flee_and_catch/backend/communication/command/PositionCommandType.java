package flee_and_catch.backend.communication.command;

public enum PositionCommandType {
	Undefined, 
	Begin,	//Set active flag! 
	End, 	//Reset active flag!
	Start, 
	Stop, 
	Position;
}
