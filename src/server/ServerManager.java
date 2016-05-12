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
	private ServerGUI gui;
	private boolean firstStart;

	public ServerManager(ClientConnector connector) {
		// this.gui = gui;
		this.connector = connector;
		firstStart = true;
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
		if(!firstStart){
			for(Entry<InetAddress, Player> e : connector.getPlayers().entrySet()){
				if(!e.getValue().isDead()){
					e.getValue().setScore(e.getValue().getScore() + 1);
				}
				connector.sendScore();
			}
		} else {
			firstStart = false;
		}
		gameWorld = new GameWorld(connector.getPlayers());
	}
	
	public GameWorld getGameWorld(){
		return gameWorld;
	}

	

}
