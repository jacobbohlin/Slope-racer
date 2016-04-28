package test;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestApplication extends Application {
	private Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Val.createWorld();
		stage.setTitle("Falling box");
        stage.setFullScreen(false);
        stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        
        final Actor ballActor = createBallActor();
        Actor ground = addGround();
        root.getChildren().add(ballActor.getSprite());
        root.getChildren().add(ground.getSprite());
         
        //Frame events.
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        Duration duration = Duration.seconds(1.0/30.0); // Set duration for frame, 30fps.
        
        //Create an ActionEvent, on trigger it executes a world time step.
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent t) {
                //What happens every frame.
            	Val.world.step(1/30f, 10, 10);
            	ballActor.update();
//            	System.out.println(ballActor.body.getPosition().y);          	
           }
        };
 
                 
         /**
         * Set ActionEvent and duration to the KeyFrame. 
         * The ActionEvent is trigged when KeyFrame execution is over. 
         */
        KeyFrame frame = new KeyFrame(duration, ae, null,null);
        timeline.getKeyFrames().add(frame);
        
        stage.setScene(scene);
        stage.show();
        timeline.playFromStart();
	}
	
	private Actor createBallActor() {
		Circle sprite = new Circle();
		sprite.setRadius(15);
        sprite.setFill(Color.BLUE);
        
        CircleShape shape = new CircleShape();
//        (shape).setRadius(15 / Val.RATIO);
        
        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = shape;
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.position = new Vec2(300 / Val.RATIO, 10 / Val.RATIO);
        bodyDef.type = BodyType.DYNAMIC;
        
        Body body = Val.world.createBody(bodyDef);
        body.createFixture(shapeDef);
          
		return new Actor(body, sprite);		
	}
	
	private Actor addGround() {
		CenteredRectangle sprite = new CenteredRectangle();
		sprite.setHeight(20);
		sprite.setWidth(600);
        sprite.setFill(Color.BLACK);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(400 / Val.RATIO, 10 / Val.RATIO);
        
        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = shape;
        shapeDef.restitution = 0.5f;
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.position = new Vec2(300 / Val.RATIO, 590 / Val.RATIO);
        
        Body body = Val.world.createBody(bodyDef);
        body.createFixture(shapeDef);
          
		return new Actor(body, sprite);		
	}

}
