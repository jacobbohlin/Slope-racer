package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager extends TimerTask {
	private ClientConnector connector;
	private GameWorld gameWorld;
//	private ServerGUI gui;
	private boolean firstStart;

	public ServerManager(ClientConnector connector, boolean firstStart) {
		// this.gui = gui;
		this.connector = connector;
		this.firstStart = firstStart;
		System.out.println("Server Manager created");

	}

	public void startScanning() {
		Scanner scan = new Scanner(System.in);
		String s = scan.nextLine();
		if (!s.isEmpty()) {
			scan.close();
			System.out.println("Lets get ready to rumble!");
			connector.sendStartMessage();
			startGame();
		}
	}

	@Override

	public void run() {
		gameWorld.step();
		try {
			connector.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void startGame() {
		connector.sendScore();
		gameWorld = new GameWorld(connector.getPlayers(), connector);
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

}
