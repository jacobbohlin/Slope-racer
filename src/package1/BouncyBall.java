package package1;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import com.sun.javafx.Utils;

public class BouncyBall {
	//JavaFX UI for ball
    public Node node;
     
    //X and Y position of the ball in JBox2D world
    private float posX;
    private float posY;
     
    //Ball radius in pixels
    private int radius;

	private Color color;
    
    public BouncyBall(float posX, float posY, int radius, Color color){
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.color = color;
        node = create();
    }
    
    private Node create() {
    	//Create an UI for ball - JavaFX code
    	Circle ball = new Circle();
    	ball.setRadius(radius);
    	ball.setFill(color); //set look and feel
    	
    	/**
    	 * Set ball position on JavaFX scene. We need to convert JBox2D coordinates 
    	 * to JavaFX coordinates which are in pixels.
    	 */
    	ball.setLayoutX(Utility.toPixelPosX(posX)); 
    	ball.setLayoutY(Utility.toPixelPosY(posY));  	
    	
    	//Create an JBox2D body defination for ball.
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posX, posY);
        
        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;  //We need to convert radius to JBox2D equivalent
        
     // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.6f;
        fd.friction = 0.3f;        
        fd.restitution = 0.8f;
        
        /**
         * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
         * Forces, torques, and impulses can be applied to these bodies.
         */
         Body body = Utility.world.createBody(bd);
         body.createFixture(fd);
         ball.setUserData(body);
         return ball;

    }
}
