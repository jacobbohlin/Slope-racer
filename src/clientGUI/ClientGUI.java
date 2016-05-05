package clientGUI;

import java.awt.MouseInfo;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import client.Client;
import client.ConnectService;
import client.ConnectionInfo;
import client.ReceiveService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Service;
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
		// setup();
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
				if (firstDraw) {
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
//		timeline.playFromStart();
	}

	private void setup() {
		ConnectDialog dialog = new ConnectDialog();
		dialog.showAndWait();
		try {
			InetAddress ip = InetAddress.getByName(dialog.getAddress());
			ConnectionInfo.setName(dialog.getName());
			ConnectionInfo.setIP(ip);
			ConnectionInfo.setPort(8888);
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(1000);
			ConnectionInfo.setSocket(socket);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		final ConnectService cs = new ConnectService();
		final ReceiveService rs = new ReceiveService();
		for (int i = 0; i < 3; i++) {
			if(!cs.isRunning()) {
				cs.reset();
				cs.start();	
				try {
					Thread.sleep(1050);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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