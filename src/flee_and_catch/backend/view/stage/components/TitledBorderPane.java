package flee_and_catch.backend.view.stage.components;

//### IMPORTS ##############################################################################################################################
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TitledBorderPane extends StackPane {
//### CONSTANTS ############################################################################################################################

//### ATTRIBUTES ###########################################################################################################################

//### COMPONENTS ###########################################################################################################################
	
	public Label lblTitle;
	StackPane skpContent;
	Node nodContent;
	
//### CONSTRUCTORS #########################################################################################################################
		
	public TitledBorderPane(String title) {
		
		this.setStyle("-fx-content-display: top;" +
			          "-fx-border-insets: 0 0 0 0;" +
			          "-fx-border-radius: 5 5 5 5;" +
			          "-fx-border-color: #494949;" +
			          "-fx-border-width: 1.5;"
			          //"-fx-background-color: #2f4f4f"
			          );
		
		this.lblTitle = new Label(title);
		this.lblTitle.setFont(Font.font(this.lblTitle.getFont().getName(), 13));
		this.lblTitle.setStyle("-fx-translate-y: -10; -fx-translate-x: +12; -fx-background-color: #F4F4F4");
		this.lblTitle.setAlignment(Pos.BASELINE_CENTER);
		
		StackPane.setAlignment(lblTitle, Pos.TOP_LEFT);
		
		this.skpContent = new StackPane();
		this.skpContent.setPadding(new Insets(15, 15, 15, 15));
		
	    this.getChildren().addAll(this.lblTitle, this.skpContent);
      
	}
	
//### INITIAL METHODS ######################################################################################################################

//### INNER CLASSES ########################################################################################################################

//### GETTER/SETTER ########################################################################################################################

	public void setTitleText(String text) {
	   this.lblTitle.setText(text);
	}
	
	public void setIcon(Image icon) {
		this.lblTitle.setGraphic(new ImageView(icon));
	}
	
	public void bindTitleText(StringProperty titleText) {
		this.lblTitle.textProperty().bind(titleText);
	}
	
	public void setContent(Node content) {
		this.nodContent = content;
		this.skpContent.getChildren().add(this.nodContent);
	}
	
	public void setTitleBackcolor(Color color) {
		this.lblTitle.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	public void adjustTitleSize() {
		this.lblTitle.setPrefWidth(this.lblTitle.getWidth() + 10);
	}
	
//### METHODS ##############################################################################################################################
}
//### EOF ##################################################################################################################################
