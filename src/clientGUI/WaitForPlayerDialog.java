package clientGUI;

import client.ConnectionInfo;
import client.StartTask;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WaitForPlayerDialog extends Dialog<Void> {
	private final Label startMessage = new Label("Waiting for game to start \n");
	private final String message = "CONTROLS: \n \n"
			+ "Steer by moving the mouse from the center of the screen, if the mouse is located to the right of the center\n"
			+ " your ball will move to the right regardless of where it is located. \n \n"
			+ "Press \"Q\" or LEFT mouseclick to MAXIMIZE and \"E\" or RIGHT mouseclick to MINIMIZE. They share a cooldown. \n \n"
			+ "Press \"TAB\" to show stats. \n \n"
			+ "Press the \"Start\" button or \"Spacebar\" to start the game or in-game to restart/start new round. \n \n"
			+ "Press m to mute/unmute.";
	
	public WaitForPlayerDialog() {
		setTitle("PILCOMANIA"); 
		setResizable(false);
		setHeaderText(null);
		getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());
		if(ConnectionInfo.getIp().getHostName().equals("localhost")) {
			ButtonType startButtonType = new ButtonType("Start", ButtonData.OK_DONE);
			getDialogPane().getButtonTypes().addAll(startButtonType, ButtonType.CLOSE);
			Button startButton = (Button) getDialogPane().lookupButton(startButtonType);
			startButton.setOnAction((Event) -> {
				new Thread(new StartTask()).start();
			});
					
		} else {
			getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		}
		
		Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.setOnAction((Event) -> {
			System.exit(0);
		});
		
		
		startMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		startMessage.setMinWidth(getWidth());
		final Label instructions = new Label(message);
		final BorderPane layout = new BorderPane();
		layout.setTop(startMessage);
		layout.setCenter(instructions);
		layout.setPadding(new Insets(15));
		BorderPane.setMargin(startMessage, new Insets(0, 0, 25, 0));
		BorderPane.setMargin(instructions, new Insets(0, 0, 25, 0));
		BorderPane.setAlignment(startMessage, Pos.CENTER);
		getDialogPane().setContent(layout);
	}
}
