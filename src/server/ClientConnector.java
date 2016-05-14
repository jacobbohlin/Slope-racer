package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;

public class ClientConnector extends Thread {
	private HashMap<InetAddress, Player> players = new HashMap<InetAddress, Player>();
	private DatagramSocket socket;
	public boolean willSendUpdate;
	private DatagramPacket dp;
	private ServerManager manager;
	private Timer timer;
	private boolean firstStart;

	public ClientConnector() {
		firstStart = true;
		willSendUpdate = false;
		// manager = new ServerManager(this);
		dp = new DatagramPacket(new byte[1000], 1000);
		try {
			socket = new DatagramSocket(8888);
		} catch (SocketException e) {
			System.out.println("Could not create socket");
			e.printStackTrace();
		}
		timer = new Timer();

	}

	@Override
	public void run() {
		while (true) {
			try {
				dp = new DatagramPacket(new byte[1000], 1000);
				socket.receive(dp);
				String input = new String(dp.getData(), 0, dp.getLength());
				if (input.startsWith("update;")) {
					update(input.substring(7));
				} else if (input.startsWith("connect;")) {
					System.out.println("A client is trying to connect.");
					connect(input.substring(8));
				} else if (input.startsWith("start")) {
					sendStartMessage();
					startGame();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startGame() {
		timer.cancel();
		timer.purge();
		manager = new ServerManager(this, firstStart);
		manager.startGame();
		manager.getGameWorld().step();
		try {
			send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendSoundEffectCue("soundtr");
		timer = new Timer();
		timer.schedule(manager, 1000, 1000 / 60);
		if (firstStart)
			firstStart = false;
	}

	private void connect(String nickName) {
		if (players.containsKey(dp.getAddress())) {
			System.out.println("That IP has already connected");
			players.get(dp.getAddress()).setPort(dp.getPort());
		} else {
			players.put(dp.getAddress(), new Player(nickName, dp.getAddress(), dp.getPort()));
			System.out.println("A new player has connected");
		}
		Player p = players.get(dp.getAddress());
		sendAck(p);
	}

	private void update(String input) throws IOException {

		if (!input.isEmpty()) {
			String[] inputArray = input.split(";");
			int mouseX = (int) Double.parseDouble(inputArray[0]);
			// System.out.println(inputArray[0]);
			int mouseY = (int) Double.parseDouble(inputArray[1]);
			int mouseClick = (int) Double.parseDouble(inputArray[2]);
			players.get(dp.getAddress()).setMousePos(mouseX, mouseY, mouseClick);
		}
	}

	public void send() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(manager.getGameWorld().getPlayerData());
		// System.out.println(manager.getGameWorld().getPlayerData()[0][0]);
		// System.out.println(GameWorld.getPlayerData()[0][1]);
		byte[] buf = baos.toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			packet.setAddress(e.getKey());
			packet.setPort(e.getValue().getPort());
			// System.out.println("sending update to: " + packet.getAddress() +
			// " " + packet.getPort() + " X: "
			// + GameWorld.getPlayerData()[e.getValue().getPlayerNbr()][0] + "
			// Y: " +
			// GameWorld.getPlayerData()[e.getValue().getPlayerNbr()][1]);
			socket.send(packet);
		}
	}

	private void sendAck(Player p) {
		String message = "ACK;" + p.getPlayerNbr();
		byte[] buf = message.getBytes();
		dp = new DatagramPacket(buf, buf.length);
		dp.setAddress(p.getAddress());
		dp.setPort(p.getPort());
		System.out.println("Sending ack to: " + dp.getAddress() + " " + dp.getPort() + " Message: "
				+ new String(dp.getData(), 0, dp.getLength()));
		try {
			socket.send(dp);
		} catch (IOException e) {
			System.out.println("Something went wrong while sending ACK");
			e.printStackTrace();
		}
	}
	
	public void sendSoundEffectCue(String soundEffect) {
		String message = "playsound;" + soundEffect;
		byte[] buf = message.getBytes();
		DatagramPacket soundpacket = new DatagramPacket(buf, buf.length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			soundpacket.setAddress(e.getKey());
			soundpacket.setPort(e.getValue().getPort());
			try {
				socket.send(soundpacket);
//				System.out.println("Sent sound to: " + soundpacket.getAddress() + " " + soundpacket.getPort() + " Message: "
//						+ new String(soundpacket.getData(), 0, soundpacket.getLength()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return HashMap containing all the Players currently connected
	 */
	public synchronized HashMap<InetAddress, Player> getPlayers() {
		return players;
	}

	public void sendStartMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("start");
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			sb.append(";" + e.getValue().getName());
		}
		byte[] buf = sb.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			packet.setAddress(e.getKey());
			packet.setPort(e.getValue().getPort());
			try {
				socket.send(packet);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void sendScore() {
		StringBuilder sb = new StringBuilder();
		sb.append("score");
		int[] scores = new int[players.size()];
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			scores[e.getValue().getPlayerNbr()] = e.getValue().getScore();
			System.out.println(e.getValue().getScore());
		}
		for (int i : scores) {
			sb.append(";" + i);
		}
		DatagramPacket dp = new DatagramPacket(sb.toString().getBytes(), sb.toString().getBytes().length);
		for (Entry<InetAddress, Player> e : players.entrySet()) {
			dp.setAddress(e.getValue().getAddress());
			dp.setPort(e.getValue().getPort());
			try {
				System.out.println("Sending score");
				socket.send(dp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		ClientConnector connector = new ClientConnector();
		connector.start();
	}

}
