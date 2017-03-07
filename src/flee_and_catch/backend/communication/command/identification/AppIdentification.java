package flee_and_catch.backend.communication.command.identification;

import flee_and_catch.backend.communication.command.device.robot.RoleType;

public class AppIdentification extends Identification {

	private String roletype;
	
	public AppIdentification(int pId, String pRoleType) {
		this.id = pId;
		this.type = IdentificationType.valueOf(IdentificationType.App.toString()).toString();
		this.roletype = RoleType.valueOf(pRoleType).toString();
	}
	
	public AppIdentification(int pId, String pType, String pRoleType) {
		this.id = pId;
		this.type = IdentificationType.valueOf(pType).toString();
		this.roletype = RoleType.valueOf(pRoleType).toString();
	}

	public String getRoletype() {
		return roletype;
	}
}
