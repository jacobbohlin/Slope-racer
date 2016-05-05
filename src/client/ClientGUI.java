package client;

import java.awt.MouseInfo;
import java.io.IOException;
import java.net.InetAddress;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ClientGUI extends Application {
	private static final float RATIO = 30;
	private Stage stage;
	private Client client;
	private Group root;
	private final double HEIGHT = 600, WIDTH = 800;
	private final Color[] COLORES = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW };
	private Circle[] circles;
//	private WaitForPlayerDialog d;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		root = new Group();
		setup();
		stage.setTitle("Slope Racer");
		stage.setFullScreen(false);
		stage.setResizable(false);
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		drawStage();
		
		stage.setScene(scene);
		stage.show();
	}
	
	private void setup() {
		Dialog dialog = new Dialog<>();
		dialog.setTitle("Slope Racer");
		dialog.setHeaderText(null);
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CLOSE);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField nickname = new TextField();
//		nickname.setPromptText("James");
		nickname.setText("JAMES");//TA BORT
		TextField address = new TextField();
//		address.setPromptText("127.0.0.1");
		address.setText("val-6");//TA BORT

		grid.add(new Label("Nickname:"), 0, 0);
		grid.add(nickname, 1, 0);
		grid.add(new Label("IP-address:"), 0, 1);
		grid.add(address, 1, 1);

		Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
//		connectButton.setDisable(true);
//		nickname.textProperty().addListener((observable, oldValue, newValue) -> {
//			connectButton.setDisable(newValue.trim().isEmpty());
//		});

		dialog.getDialogPane().setContent(grid);

		dialog.showAndWait();
		for (;;) {
			try {
				InetAddress ip = InetAddress.getByName(address.getText());
				client = new Client(this, ip, 8888, nickname.getText());
//				d = new WaitForPlayerDialog();
				break;
			} catch (IOException e) {
				dialog.setHeaderText("Invalid IP");
				dialog.showAndWait();
			} catch (Client.ConnectionException e1) {
				dialog.setHeaderText("Unable to connect");
				dialog.showAndWait();
			}
		}
	}
	
	/**
	 * Draws the static game stage.
	 */
	private void drawStage() {
//		d.close();
		Rectangle floor = new Rectangle(WIDTH, 15, Color.BLACK);
		floor.setX(0);
		floor.setY(HEIGHT - 15);
		root.getChildren().add(floor);
	}
	
	/**
	 * Draws the player figures and places them in their initial position.
	 * @param pos Position of all players.
	 */
	public void initialDraw(float[][] pos) {
		System.out.println("-------------------------Drawing circles------------------------");
		circles = new Circle[pos.length];
		float[] indivPos = new float[2/* pos[0].length */];
		for (int i = 0; i < pos.length; i++) {
			for (int k = 0; k < 2 /* pos[i].length */; k++) {
				indivPos[k] = pos[i][k]*RATIO;
			}
			circles[i] = new Circle(20, COLORES[i]);
			circles[i].setLayoutX(indivPos[0]+100);
			circles[i].setLayoutY(indivPos[1]+100);
			root.getChildren().add(circles[i]);
		}
	}
	
	/**
	 * Updates position of all players.
	 * @param pos Positions of all players.
	 */
	public void update(float[][] pos) {
		System.out.println("---------------UPDATING-------------");
		float[] indivPos = new float[2/* pos[0].length */];
		for (int i = 0; i < pos.length; i++) {
			for (int k = 0; k < 2 /* pos[i].length */; k++) {
				indivPos[k] = pos[i][k]*RATIO;
			}
			circles[i].setLayoutX(indivPos[0]);
			circles[i].setLayoutY(indivPos[1]);
		}
	}
	
	/**
	 * @return Mouse's X-position.
	 */
	public int getMouseX() {
		return (int) MouseInfo.getPointerInfo().getLocation().getX();
		//TODO Update to percentage of screen instead of pixels.
	}
	
	/**
	 * @return Mouse's Y-position.
	 */
	public int getMouseY() {
		return (int) MouseInfo.getPointerInfo().getLocation().getY();
		//TODO Update to percentage of screen instead of pixels.
	}
	
	private class WaitForPlayerDialog extends Dialog {
		
		private WaitForPlayerDialog() {
			setTitle("Slope Racer");
			setHeaderText(null);
			setContentText("Waiting for game to start...");
			showAndWait();
			
		}
	}
}
