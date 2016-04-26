package test;

import javafx.scene.shape.Rectangle;

public class CenteredRectangle extends Rectangle {
	
	public CenteredRectangle () {
		super();
	}
	
	public void setLayoutX(Double x) {
		super.setLayoutX(x - super.getWidth()/2);
	}
	
	public void setLayoutY(Double y) {
		super.setLayoutY(y - super.getHeight()/2);
	}
}
