package clientGUI;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import server.ClientConnector;

public class OpenDialog extends Dialog<Void>  {
	
	public OpenDialog(ConnectDialog dialog) {
		setTitle("Pilcomania");
		setResizable(false);
		setHeaderText("Welcome to Pilcomania!");
		
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		ButtonType createButtonType = new ButtonType("Create Server", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(createButtonType, connectButtonType, ButtonType.CLOSE);
		getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());
		
		Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.setOnAction((Event) -> {
			System.exit(0);
		});
		closeButton.setText("Quit");
		
		Button createServerButton = (Button) getDialogPane().lookupButton(createButtonType);
		createServerButton.setOnAction((Event) -> {
			new ClientConnector().start();
			dialog.createServer();
		});		
		getDialogPane().setMinWidth(400);
	}
}
