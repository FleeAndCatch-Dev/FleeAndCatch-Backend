package flee_and_catch.backend.communication.command.identification;

import flee_and_catch.backend.communication.command.device.robot.RobotType;
import flee_and_catch.backend.communication.command.device.robot.RoleType;

public class RobotIdentification extends Identification {
	private String subtype;
	private String roletype;
	
	public RobotIdentification(int pId, String pSubtype, String pRoletype){
		this.id = pId;
		this.type = IdentificationType.valueOf(IdentificationType.Robot.toString()).toString();
		this.subtype = RobotType.valueOf(pSubtype).toString();
		this.roletype = RoleType.valueOf(pRoletype).toString();
	}
	
	public RobotIdentification(int pId, String pType, String pSubtype, String pRoletype){
		this.id = pId;
		this.type = IdentificationType.valueOf(pType).toString();
		this.subtype = RobotType.valueOf(pSubtype).toString();
		this.roletype = RoleType.valueOf(pRoletype).toString();
	}

	public String getSubtype() {
		return this.subtype;
	}

	public String getRole() {
		return this.roletype;
	}
}
