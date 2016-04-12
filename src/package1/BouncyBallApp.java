package package1;


import org.jbox2d.dynamics.Body;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BouncyBallApp extends Application {

	public static void main(String [] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Bouncy Ball");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        Group root = new Group(); //Create a group for holding all objects on the screen
        Scene scene = new Scene(root, Utility.WIDTH, Utility.HEIGHT);
        
      //create ball   
        final BouncyBall ball = new BouncyBall(45, 90, Utility.BALL_RADIUS, Color.RED);
          
        //Add ground to the application, this is where balls will land
        Utility.addGround(100, 10);
         
        //Add left and right walls so balls will not move outside the viewing area.
        Utility.addWall(0,100,1,100); //Left wall
        Utility.addWall(99,100,1,100); //Right wall

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
 
        Duration duration = Duration.seconds(1.0/60.0); // Set duration for frame.
         
        //Create an ActionEvent, on trigger it executes a world time step and moves the balls to new position 
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent t) {
                        //Create time step. Set Iteration count 8 for velocity and 3 for positions
                        Utility.world.step(1.0f/60.f, 8, 3); 
                        
                        //Move balls to the new position computed by JBox2D
                        Body body = (Body)ball.node.getUserData();
                        float xpos = Utility.toPixelPosX(body.getPosition().x);
                        float ypos = Utility.toPixelPosY(body.getPosition().y);
                        ball.node.setLayoutX(xpos);
                        ball.node.setLayoutY(ypos);
           }
        };
 
                 
         /**
         * Set ActionEvent and duration to the KeyFrame. 
         * The ActionEvent is trigged when KeyFrame execution is over. 
         */
        KeyFrame frame = new KeyFrame(duration, ae, null,null);
 
        timeline.getKeyFrames().add(frame);
        
      //Create button to start simulation.
        final Button btn = new Button();
        btn.setLayoutX((Utility.WIDTH/2) -15);
        btn.setLayoutY((Utility.HEIGHT-30));
        btn.setText("Start");
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent event) {
                        timeline.playFromStart(); 
                        btn.setVisible(false);
            }
        });
        
        root.getChildren().add(btn);
        root.getChildren().add(ball.node);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
}
