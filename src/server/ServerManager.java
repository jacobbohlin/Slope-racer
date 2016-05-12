package server;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager extends TimerTask {
	private ClientConnector connector;
	private GameWorld world;
	private ServerGUI gui;

	public ServerManager() {
		// this.gui = gui;
		connector = new ClientConnector();
		connector.start();
		System.out.println("Server started");
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
		world.step();
		try {
			connector.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startGame() {
		world = new GameWorld(connector.getPlayers());
		Timer timer = new Timer();
		timer.schedule(this, 0, 1000 / 60);
	}

	public static void main(String args[]) {
		ServerManager man = new ServerManager();
		man.startScanning();
	}

}
