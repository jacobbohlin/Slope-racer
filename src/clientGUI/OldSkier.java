package clientGUI;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class OldSkier {
	private Rectangle torso, arms,  legs, boots, calves, skis;
	private Circle head;
	private final int RATIO = 30;
	
	public OldSkier() {
		torso = new Rectangle(0.25 * RATIO, 1 * RATIO);
		arms = new Rectangle(0.15 * RATIO, 1 * RATIO);
		legs = new Rectangle(0.25 * RATIO, 0.5 * RATIO);
		calves = new Rectangle(0.2 * RATIO, 0.3 * RATIO);
		boots = new Rectangle(0.4 * RATIO, 0.2 * RATIO);
		skis = new Rectangle(2 * RATIO, 0.02 * RATIO);
		head = new Circle(0.08 * RATIO);
		bindTorsoToHead();
		bindCalvesToSkis();
		
	}
	
	public void setTorsoX(float x) {
		torso.setLayoutX(x - torso.getWidth()/2);
	}
	
	public void setTorsoY(float y) {
		torso.setLayoutY(y - torso.getHeight()/2);
	}
	
	/**
	 * Binds the Head and Torso together.
	 */
	private void bindTorsoToHead() {
		Line line = new Line();
		line.startXProperty().bind(head.centerXProperty());
		line.startYProperty().bind(head.centerYProperty());
		line.endXProperty().bind(torso.xProperty().add(torso.getWidth()/2));
		line.endYProperty().bind(torso.yProperty());
	}
	
	/**
	 * Binds the Calves, Boots and Skis together.
	 */
	private void bindCalvesToSkis() {
		Line line0 = new Line();
		line0.startXProperty().bind(calves.xProperty().add(calves.getWidth()/2));
		line0.startYProperty().bind(calves.yProperty().add(calves.getHeight()));
		line0.endXProperty().bind(boots.xProperty().add(calves.getWidth()/2));
		line0.endYProperty().bind(boots.yProperty());
		Line line1 = new Line();
		line1.startXProperty().bind(boots.xProperty().add(boots.getWidth()/2));
		line1.startYProperty().bind(boots.yProperty().add(boots.getHeight()));
		line1.endXProperty().bind(skis.xProperty().add(skis.getWidth()/3));
		line1.endYProperty().bind(skis.yProperty());
	}
}
