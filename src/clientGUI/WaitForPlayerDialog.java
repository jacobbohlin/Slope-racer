package clientGUI;

import client.StartTask;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class WaitForPlayerDialog extends Dialog<Void> {
	private final String message = "Waiting for game to start";
	
	public WaitForPlayerDialog() {
		setTitle("Slope Racer");
		setResizable(false);
		setHeaderText(null);
		getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());
		ButtonType startButtonType = new ButtonType("Start", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(startButtonType, ButtonType.CLOSE);
		Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.setOnAction((Event) -> {
			System.exit(0);
		});
		
		Button startButton = (Button) getDialogPane().lookupButton(startButtonType);
		startButton.setOnAction((Event) -> {
			new Thread(new StartTask()).start();
		});
		setContentText(message);
	}
}
