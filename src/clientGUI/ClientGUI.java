package clientGUI;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import client.ConnectTask;
import client.ConnectionInfo;
import client.ReceiveService;
import client.SendTask;
import client.SoundEffectHandler;
import client.StartTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientGUI extends Application {
	private final double ASPECT_RATIO = 16/9;
	private double width, height, widthMargin, heightMargin;
	private float RATIO;
	private boolean firstDraw = true;
	private boolean secondDraw = true;
	private final Color[] COLORES = { Color.BLUE, Color.RED, Color.GREEN, Color.WHEAT, Color.BLACK, Color.BLUEVIOLET };
	private final String[] SCORE_COLORES = {"blue", "red", "green", "wheat", "black", "blueviolet"};
	private Group root;
	private Circle[] circles;
	private final Timeline timeline = new Timeline();
	private final float FPS = 1 / 60f;
	private ConnectDialog connectDialog;
	private WaitForPlayerDialog waitDialog;
	private int ability = -1;
	private VBox scoreboard;
	private Label[] names, scores;
	private int boostImageCounter = 0;
	private Level level;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		root = new Group();
		
		SoundEffectHandler seh = new SoundEffectHandler();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final double SCREEN_WIDTH = screen.getWidth();
		final double SCREEN_HEIGHT = screen.getHeight();
		
		if(SCREEN_WIDTH / SCREEN_HEIGHT > ASPECT_RATIO) {
			width = SCREEN_WIDTH;
			height = (SCREEN_WIDTH / 16) * 9;
		} else if (SCREEN_WIDTH / SCREEN_HEIGHT < ASPECT_RATIO) {
			width = (SCREEN_HEIGHT / 9) * 16;
			height = SCREEN_HEIGHT;
		} else {
			width = SCREEN_WIDTH;
			height = SCREEN_HEIGHT;
		}
		RATIO = (float) (height / 18);
		widthMargin = (SCREEN_WIDTH - width) / 2;
		heightMargin = (SCREEN_HEIGHT - height) / 2;
		stage.setTitle("PILCOMANIA");
		stage.setFullScreen(true);
		stage.setResizable(false);
		Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Frame events.
		timeline.setCycleCount(Timeline.INDEFINITE);

		Duration duration = Duration.seconds(FPS); // Set duration for
													// frame, 60fps.

		// Create an ActionEvent, on trigger it executes a world time step. 
		EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent t) {
				// What happens every frame.
				if(boostImageCounter == 20) {
					boostImageCounter = 0;
					level.update();
				}
				if (ConnectionInfo.isFirstPacketReceived()) {
					if(waitDialog.isShowing()) {
						waitDialog.close();
					}
					if (firstDraw) {
						firstDraw = false;
						initialDraw();
					} else {
						update();
					}
					new Thread(new SendTask(getMouseX(), getMouseY(), ability)).start();
					ability = -1;
				}
				boostImageCounter++;
			}
		};

		/**
		 * Set ActionEvent and duration to the KeyFrame. The ActionEvent is
		 * trigged when KeyFrame execution is over.
		 */
		KeyFrame frame = new KeyFrame(duration, ae, null, null);
		timeline.getKeyFrames().add(frame);
		
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {

            if( e.isPrimaryButtonDown() && e.isSecondaryButtonDown()) {
            } else if( e.isPrimaryButtonDown()) {
                ability = 0;
            } else if( e.isSecondaryButtonDown()) {
                ability = 1;
            }
        });
		
		createScoreboard();
		root.getChildren().add(scoreboard);
		
		scene.setOnKeyPressed(ke ->{
			if(ke.getCode() == KeyCode.Q) {
				ability = 0;
			} else if(ke.getCode() == KeyCode.E) {
				ability = 1;
			} else if(ke.getCode() == KeyCode.SPACE) {
				if(ConnectionInfo.getIp().getHostName().equals("localhost")) {
					new Thread(new StartTask()).start();				
				}
			} else if(ke.getCode() == KeyCode.TAB) {
				if(!ConnectionInfo.isFirstPacketReceived()) return;
				String[] playerNames = ConnectionInfo.getPlayerNames();
				String[] playerScores = ConnectionInfo.getScore();
				if(playerNames == null) return;
				for(int i = 0; i < playerNames.length; i++) {
					names[i+1].setText(playerNames[i] + ": ");
					scores[i+1].setText(playerScores[i]);
				}
				scoreboard.setVisible(true);
			} else if(ke.getCode() == KeyCode.M) {
				seh.mute();
			}
		});
		
		scene.setOnKeyReleased(ke ->{
			if(ke.getCode() == KeyCode.TAB) {
				scoreboard.setVisible(false);
			}
		});
		
		stage.setScene(scene);
		stage.show();
		connectDialog = new ConnectDialog();
		setup();
	}
	
	private void createScoreboard() {
		scoreboard = new VBox();
		names = new Label[7];
		scores = new Label[7];
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(5, 20, 5, 20));
		names[0] = new Label("Name");
		scores[0] = new Label("Score");
		names[0].setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		scores[0].setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		layout.add(names[0], 0, 0);
		layout.add(scores[0], 2, 0);
		for(int i = 1; i < 7; i++) {
			names[i] = new Label();
			scores[i] = new Label();
			names[i].setTextFill(COLORES[i - 1]);
			scores[i].setTextFill(COLORES[i - 1]);
			names[i].setFont(Font.font("Verdana", 20));
			scores[i].setFont(Font.font("Verdana", 20));
			layout.add(names[i], 0, i);
			layout.add(scores[i], 1, i);
			
		}
		scoreboard.getChildren().add(layout);
		VBox.setVgrow(layout, Priority.ALWAYS);
		scoreboard.setPrefWidth(350);
		scoreboard.setLayoutX(80 + widthMargin);
		scoreboard.setLayoutY(80 + heightMargin);
		scoreboard.setVisible(false);
		
		
		
	}

	/**
	 * Shows the setup dialog where the user enters their name and IP of the
	 * server.
	 */
	private void setup() {
		for (;;) {
			try {
				connectDialog.showAndWait();
				InetAddress ip = InetAddress.getByName(connectDialog.getAddress());
				ConnectionInfo.setName(connectDialog.getName());
				ConnectionInfo.setIp(ip);
				ConnectionInfo.setPort(8888);
				DatagramSocket socket = new DatagramSocket();
				ConnectionInfo.setSocket(socket);
				break;
			} catch (UnknownHostException e) {
				connectDialog.wrongIP();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		}
		for (int i = 0; i < 3; i++) {
			new Thread(new ConnectTask()).start();
			try {
				Thread.sleep(1010);
				if (ConnectionInfo.getId() != -1) {
					break;
				}
				if (i == 2) {
					connectDialog.timeOut();
					setup();
				}
			} catch (InterruptedException e) {
				connectDialog.timeOut();
			}
		}
		waitDialog = new WaitForPlayerDialog();
		waitDialog.show();
		ReceiveService rs = new ReceiveService();
		rs.start();
		level = new StandardLevel(root, width, height, widthMargin, heightMargin, RATIO);
		timeline.playFromStart();
	}

	/**
	 * Draws the player figures and places them in their correct position.
	 * 
	 * @param pos
	 *            Position of all players.
	 */
	public void initialDraw() {
		float[][] playerData = ConnectionInfo.getPlayerData();
		circles = new Circle[playerData.length];
		float[] indivValues = new float[playerData[0].length];
		for (int i = 0; i < playerData.length; i++) {
			for (int k = 0; k < playerData[i].length; k++) {
				indivValues[k] = playerData[i][k] * RATIO;
			}
			circles[i] = new Circle();
			circles[i].setLayoutX(indivValues[0] + widthMargin);
			circles[i].setLayoutY(indivValues[1] + heightMargin);
			circles[i].setRadius(indivValues[2]);
			circles[i].setStroke(COLORES[i]);
			circles[i].setStrokeWidth(5);
			circles[i].setFill(Color.AZURE);
			root.getChildren().add(circles[i]);
			if(i != ConnectionInfo.getId()) {
				circles[i].setVisible(false);
			}
		}
	}

	/**
	 * Updates position of all players.
	 * 
	 * @param pos
	 *            Positions of all players.
	 */
	public void update() {
		if(secondDraw) {
			for(Circle c: circles) {
				c.setVisible(true);
			}
			secondDraw = false; 
		}
		float[][] playerData = ConnectionInfo.getPlayerData();
		float[] indivValues = new float[playerData[0].length];
		for (int i = 0; i < playerData.length; i++) {
			for (int k = 0; k < playerData[i].length; k++) {
				indivValues[k] = playerData[i][k] * RATIO;
			}
			circles[i].setLayoutX(indivValues[0] + widthMargin);
			circles[i].setLayoutY(indivValues[1] + heightMargin);
			circles[i].setRadius(indivValues[2]);
		}
	}

	/**
	 * @return Mouse's X-position in percentage from upper-left corner.
	 */
	private float getMouseX() {
		return (float) (MouseInfo.getPointerInfo().getLocation().getX() / width * 100);

	}

	/**
	 * @return Mouse's Y-position in percentage from upper-left corner.
	 */
	private float getMouseY() {
		return (float) (MouseInfo.getPointerInfo().getLocation().getY() / height * 100);
	}
}
