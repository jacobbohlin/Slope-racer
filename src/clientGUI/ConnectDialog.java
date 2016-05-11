package clientGUI;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class ConnectDialog extends Dialog<Void> {
	private TextField address;
	private TextField nickname;
	
	/**
	 * Constructs a dialog with two Text input fields for nickname and IP-address of the server.
	 */
	public ConnectDialog() {
		setTitle("Slope Racer");
		setResizable(false);
		setHeaderText(null);
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CLOSE);
		getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		nickname = new TextField();
		// nickname.setPromptText("James");
		nickname.setText("JAMES");// TA BORT
		address = new TextField();
		// address.setPromptText("127.0.0.1");
		address.setText("falk-2");// TA BORT

		grid.add(new Label("Nickname:"), 0, 0);
		grid.add(nickname, 1, 0);
		grid.add(new Label("IP-address:"), 0, 1);
		grid.add(address, 1, 1);

		Node connectButton = getDialogPane().lookupButton(connectButtonType);
		// connectButton.setDisable(true);
		// nickname.textProperty().addListener((observable, oldValue, newValue)
		// -> {
		// connectButton.setDisable(newValue.trim().isEmpty());
		// });
		Button closeButton = (Button) getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.setOnAction((Event) -> {
			System.exit(0);
		});
		getDialogPane().setContent(grid);
	}
	
	/**
	 * @return The input of the IP-address text field.
	 */
	public String getAddress() {
		return address.getText();
	}
	
	/**
	 * @return The input of the Name text field.
	 */
	public String getName() {
		return nickname.getText();
	}
	
	/**
	 * Sets the header text if the IP-address is invalid.
	 */
	public void wrongIP() {
		setHeaderText("Invalid IP-address.");
	}
	
	public void timeOut() {
		setHeaderText("Unable to connect.");
	}
}
