package test;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FallingBoxes extends Application {
	private final float RATIO = 30; //Divide all coordinates by this ratio to convert pixels to meters.
	private World world;
	private BodyDef boxDef;
	private int frame = -1;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		createWorld();
		primaryStage.setTitle("Falling boxes");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        
        //FPS handling.
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
 
        Duration duration = Duration.seconds(1.0/60.0);

        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
        
        	public void handle(ActionEvent t) {
        		if((frame++ % 60) == 0) {
        			System.out.println("Location of box at frame " + (frame-1) + ": " + boxDef.position.x + " " + boxDef.position.y);			
        		}
        		world.step(1.0f/60.f, 8, 3); 
        	}    	
        };
        	
        KeyFrame frame = new KeyFrame(duration, ae, null,null);
        timeline.getKeyFrames().add(frame);
        
        final Button btn = new Button();
        btn.setLayoutX(scene.getWidth()/2);
        btn.setLayoutY(scene.getHeight() - 25);
        btn.setText("Start");
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent event) {
                        timeline.playFromStart(); 
                        btn.setVisible(false);
            }
        });
        
        root.getChildren().add(btn);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("Number of bodies in world: " + world.getBodyCount());
	}
	
	/**
	 * Creates the JBox2D world and calls methods for creating the environment.
	 */
	private void createWorld() {
		world = new World(new Vec2(0.0f, -9.82f), true);
		addGround();
		addWalls();
		addBox();
	}
	
	/**
	 * Adds a floor to the world.
	 */
	private void addGround() {	
		//Creates the rectangular Shape.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(400/RATIO, 10/RATIO);//Division by ratio for the pixel to meter convertion.
		//The parameters is half its width and half its height.
		
		//Creates the Shapes definition.
		FixtureDef shapeDef = new FixtureDef();
		shapeDef.shape = shape;
		shapeDef.restitution = 0.3f; //Bounciness. Means the velocity will be multiplied by 0.3.
		//Leaving Friction at its standard 0.2f and density at 0.0f since it is a static body.
		
		//Creates the Body definition, specifies where the body will be located(in m).
		BodyDef bodyDef = new BodyDef();
		bodyDef.position = new Vec2(400/RATIO, 295/RATIO);//This is the position of the objects center of gravity.
		
		//Creates the actual body form its definition and inserts it in to the world.
		//Also applies the shape definition to describe the object.
		Body body = world.createBody(bodyDef);
		body.createFixture(shapeDef);
		
//		Rectangle rectangle = new Rectangle();
//		rectangle.setHeight(20);
//		rectangle.setWidth(800);
//		rectangle.setFill(Color.BLACK);
//		rectangle.setLayoutX(50);
//		rectangle.setLayoutY(50);
//		rectangle.setUserData(body);
	}
	
	/**
	 * Adds both walls on both the right and left side of the world.
	 */
	private void addWalls() {
		//Basically the same procedure as the addGround() method to begin with.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10/RATIO, 300/RATIO);
		
		FixtureDef shapeDef = new FixtureDef();
		shapeDef.shape = shape;
		shapeDef.restitution = 0.3f;
		
		//Definition for the Left wall.
		BodyDef bodyDef = new BodyDef();
		bodyDef.position = new Vec2(5/RATIO, 300/RATIO);
		
		world.createBody(bodyDef).createFixture(shapeDef);
		
		//Redefining bodyDef to the Right wall.
		bodyDef.position = new Vec2(795/RATIO, 300/RATIO);
		
		world.createBody(bodyDef).createFixture(shapeDef);
	}

	/**
	 * Adds a falling box to the world.
	 */
	private void addBox() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(25/RATIO, 25/RATIO);
		
		FixtureDef shapeDef = new FixtureDef();
		shapeDef.shape = shape;
		shapeDef.restitution = 0.3f;
		shapeDef.density = 1.0f;//Density 1.0f is the density of water.
		
		boxDef = new BodyDef();
		boxDef.type = BodyType.DYNAMIC;//Setting the type to DYNAMIC will make it able to move.
		boxDef.position = new Vec2(400/RATIO, -50/RATIO);
		//Since the y-coordinate is below zero the box will spawn over the visible field.
		
		Body box = world.createBody(boxDef);
		box.createFixture(shapeDef);
		box.resetMassData();
//		world.createBody(boxDef).createFixture(shapeDef);
		
	}
	
	

}
