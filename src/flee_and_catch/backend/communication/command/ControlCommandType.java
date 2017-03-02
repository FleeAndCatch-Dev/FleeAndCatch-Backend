package flee_and_catch.backend.communication.command;

public enum ControlCommandType {
	Undefined, 
	Begin,	//Set active flag! 
	End, 	//Reset active flag!
	Start, 
	Stop, 
	Control;
}
