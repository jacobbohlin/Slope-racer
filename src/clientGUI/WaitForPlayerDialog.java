package clientGUI;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class WaitForPlayerDialog extends Dialog<Void> {
	private final String message = "Waiting for game to start";
	
	public WaitForPlayerDialog() {
		setTitle("Slope Racer");
		setResizable(false);
		setHeaderText(null);
		getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());
		getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.setOnAction((Event) -> {
			System.exit(0);
		});
		setContentText(message);
	}
}
