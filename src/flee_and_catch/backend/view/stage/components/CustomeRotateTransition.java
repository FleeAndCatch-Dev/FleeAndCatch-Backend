package flee_and_catch.backend.view.stage.components;


import javafx.animation.RotateTransition;
import javafx.scene.control.Button;

public class CustomeRotateTransition extends Button {

	private RotateTransition rotTrans;
	
	
	public RotateTransition getRotateTransition() {
		return rotTrans;
	}

	public void setRotateTransition(RotateTransition rotTrans) {
		this.rotTrans = rotTrans;
	}
	
}
