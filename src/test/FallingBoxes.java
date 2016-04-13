package test;

import package1.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FallingBoxes extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Bouncy Ball");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        Scene scene = new Scene(new Pane(), 800, 600);
        
        //FPS handling.
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
 
        Duration duration = Duration.seconds(1.0/60.0);
        
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
        	
        	public void handle(ActionEvent t) {
        		
        	}    	
        };
        	
        KeyFrame frame = new KeyFrame(duration, ae, null,null);
        timeline.getKeyFrames().add(frame);
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
