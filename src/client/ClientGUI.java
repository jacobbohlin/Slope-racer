package client;

import java.awt.MouseInfo;
import java.io.IOException;
import java.net.InetAddress;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.util.Duration;
import test.Val;

public class ClientGUI extends Application {
	private boolean firstDraw = true;
	private static final float RATIO = 30;
	private final Color[] COLORES = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW };
	private Stage stage;
	private Client client;
	private Group root;
	private final double HEIGHT = 600, WIDTH = 800;
	private Circle[] circles;
	// private WaitForPlayerDialog d;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		root = new Group();
//		setup();
		stage.setTitle("Slope Racer");
		stage.setFullScreen(false);
		stage.setResizable(false);

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		drawStage();

		// Frame events.
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		Duration duration = Duration.seconds(1.0 / 30.0); // Set duration for
															// frame, 30fps.

		// Create an ActionEvent, on trigger it executes a world time step.
		EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent t) {
				// What happens every frame.
				if(firstDraw) {
					initialDraw();
				} else {
					update();
				}
				client.sendUpdate(getMouseX(), getMouseY());

			}
		};

		/**
		 * Set ActionEvent and duration to the KeyFrame. The ActionEvent is
		 * trigged when KeyFrame execution is over.
		 */
		KeyFrame frame = new KeyFrame(duration, ae, null, null);
		timeline.getKeyFrames().add(frame);

		stage.setScene(scene);
		stage.show();
		System.out.println("Showing GUI");
		setup();
		timeline.playFromStart();
	}

	private void setup() {
		Dialog dialog = new Dialog();
		dialog.setTitle("Slope Racer");
		dialog.setHeaderText(null);
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CLOSE);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField nickname = new TextField();
		// nickname.setPromptText("James");
		nickname.setText("JAMES");// TA BORT
		TextField address = new TextField();
		// address.setPromptText("127.0.0.1");
		address.setText("val-6");// TA BORT

		grid.add(new Label("Nickname:"), 0, 0);
		grid.add(nickname, 1, 0);
		grid.add(new Label("IP-address:"), 0, 1);
		grid.add(address, 1, 1);

		Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
		// connectButton.setDisable(true);
		// nickname.textProperty().addListener((observable, oldValue, newValue)
		// -> {
		// connectButton.setDisable(newValue.trim().isEmpty());
		// });

		dialog.getDialogPane().setContent(grid);

		dialog.showAndWait();
		for (;;) {
			try {
				InetAddress ip = InetAddress.getByName(address.getText());
				client = new Client(ip, 8888, nickname.getText());
				client.start();
				// d = new WaitForPlayerDialog();
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
		// d.close();
		Rectangle floor = new Rectangle(WIDTH, 15, Color.BLACK);
		floor.setX(0);
		floor.setY(HEIGHT - 15);
		root.getChildren().add(floor);
	}

	/**
	 * Draws the player figures and places them in their initial position.
	 * 
	 * @param pos
	 *            Position of all players.
	 */
	public void initialDraw() {
		System.out.println("-------------------------Drawing circles------------------------");
		float[][] playerData = client.getPlayerData();
		circles = new Circle[playerData.length];
		float[] indivPos = new float[2/* pos[0].length */];
		for (int i = 0; i < playerData.length; i++) {
			for (int k = 0; k < 2 /* pos[i].length */; k++) {
				indivPos[k] = playerData[i][k] * RATIO;
			}
			circles[i] = new Circle(20, COLORES[i]);
			circles[i].setLayoutX(indivPos[0] + 100);
			circles[i].setLayoutY(indivPos[1] + 100);
			root.getChildren().add(circles[i]);
		}
	}

	/**
	 * Updates position of all players.
	 * 
	 * @param pos
	 *            Positions of all players.
	 */
	public void update() {
		System.out.println("---------------UPDATING-------------");
		float[][] playerData = client.getPlayerData();
		float[] indivPos = new float[2/* pos[0].length */];
		for (int i = 0; i < playerData.length; i++) {
			for (int k = 0; k < 2 /* pos[i].length */; k++) {
				indivPos[k] = playerData[i][k] * RATIO;
			}
			circles[i].setLayoutX(indivPos[0]);
			circles[i].setLayoutY(indivPos[1]);
		}
	}

	/**
	 * @return Mouse's X-position.
	 */
	private int getMouseX() {
		return (int) MouseInfo.getPointerInfo().getLocation().getX();
		// TODO Update to percentage of screen instead of pixels.
	}

	/**
	 * @return Mouse's Y-position.
	 */
	private int getMouseY() {
		return (int) MouseInfo.getPointerInfo().getLocation().getY();
		// TODO Update to percentage of screen instead of pixels.
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
