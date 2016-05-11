package clientGUI;



import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StandardLevel extends Level {

	public StandardLevel(Group g, double width, double height, float ratio) {
		super(g, width, height, ratio);
	}
	
	@Override
	protected void buildLevel() {
		Rectangle floor = new Rectangle(0, 0, width, ratio/2);
		Rectangle roof = new Rectangle(0, height - ratio/2, width, ratio/2);
		Rectangle leftWall = new Rectangle(0, 0, ratio/2, height);
		Rectangle rightWall = new Rectangle(width - ratio/2, 0, ratio/2, height);
		Rectangle boostPad = new Rectangle(width/2 - ratio, height/2 - ratio, ratio * 2, ratio * 2);
		floor.setFill(Color.BLACK);
		roof.setFill(Color.BLACK);
		leftWall.setFill(Color.BLACK);
		rightWall.setFill(Color.BLACK);
		boostPad.setFill(Color.DARKTURQUOISE);
		g.getChildren().addAll(floor, roof, leftWall, rightWall, boostPad);
	}

}
