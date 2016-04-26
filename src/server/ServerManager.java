package server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;


public class ServerManager {
	private ClientConnector connector;
	private GameWorld world;
	private ServerGUI gui;
	
	public ServerManager(ServerGUI gui) {
		this.gui = gui;
		connector = new ClientConnector();
		world = new GameWorld();
		 //Frame events.
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        Duration duration = Duration.seconds(1.0/30.0); // Set duration for frame, 30fps.
        
        //Create an ActionEvent, on trigger it executes a world time step.
        EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
        	
            public void handle(ActionEvent t) {
                //What happens every frame.
            	//World.step bland annat. 	
            	//Set connector.Wills = true
            	
           }
        };
 
         /**
         * Set ActionEvent and duration to the KeyFrame. 
         * The ActionEvent is trigged when KeyFrame execution is over. 
         */
        KeyFrame frame = new KeyFrame(duration, ae, null,null);
        timeline.getKeyFrames().add(frame);
        
        //Start the timeline.
        timeline.playFromStart();
	}
}
