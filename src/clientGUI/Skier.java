package clientGUI;

import javafx.scene.canvas.GraphicsContext;

public class Skier {
	private BodyPart torso, arms, legs, lowerLegs;
	private BodyPart[] bodyParts;
	private final int RATIO = 30;
	
	public Skier() {
		torso = new BodyPart(0.25 * RATIO, 1.16 * RATIO);
		arms = new BodyPart(0.15 * RATIO, 1.2 * RATIO);
		legs = new BodyPart(0.25 * RATIO, 0.5 * RATIO);
		lowerLegs = new BodyPart(2 * RATIO, 0.52 * RATIO);
		bodyParts = new BodyPart[]{torso, arms, legs, lowerLegs};
	}

	public BodyPart[] getBodyParts() {
		return bodyParts;
	}
	
	public void draw(GraphicsContext gc) {
		for(int i = 0; i < bodyParts.length; i++) {
			gc.fillRect(bodyParts[i].getxPos(), bodyParts[i].getyPos(), bodyParts[i].getWidth(), bodyParts[i].getHeight());
		}
	}
	
}
