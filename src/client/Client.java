package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	private DatagramSocket socket;
	private boolean connected = false;
	private int id;
	private ClientGUI gui;
	private String name;
	private boolean firstDraw = true;
	private InetAddress ip;
	private int port;
	 
	

	public Client(ClientGUI gui, InetAddress ip, int port, String name) throws IOException, ConnectionException {
		this.gui = gui;
		this.name = name;
		this.ip = ip;
		this.port = port;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Runner r = new Runner();
		r.start();
		try {
			int i = 0;
			while (!connected && i < 3) {
				establishConnection();
				i++;
				Thread.sleep(3000);
				if(i == 2) {
					throw new ConnectionException(); 
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Connection timed-out.");
		}
	}

	private void establishConnection() throws IOException {
		byte[] handshake = ("connect;" + name).getBytes();
		DatagramPacket connect = new DatagramPacket(handshake, handshake.length);
		connect.setAddress(ip);
		connect.setPort(port);
		socket.send(connect);
		System.out.println("Trying to connect...");

	}
	
	private void sendUpdate() {
		byte[] buf = ("update;" + gui.getMouseX() + ";" + gui.getMouseY()).getBytes();		
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		dp.setAddress(ip);
		dp.setPort(port);
		try {
			socket.send(dp);
		} catch (IOException e) {
			System.out.println("Could not send update to server");
			e.printStackTrace();
		}
	}

	private class Runner extends Thread {

		@Override
		public void run() {
			byte[] buf = new byte[1000];
			DatagramPacket response = new DatagramPacket(buf, buf.length);
			for (;;) {
				try {
					socket.receive(response);
					String s = new String(response.getData(), 0, response.getLength());

					/* This happens while the connection is not yet established
					and the client is waiting for ACK.*/
					if (!connected) {
						if (s.startsWith("ACK")) {
							id = Integer.parseInt(s.split(";")[1]);
							System.out.println("FEEDBACKZZ");
							connected = true;
							System.out.println("My player id is: " + id);
						} else {
							System.out.println("Connection DENIED!");
						}
						// End of connection-process.
						
					} else if (!s.startsWith("ACK")) {
						ByteArrayInputStream bais = new ByteArrayInputStream(buf);
						ObjectInputStream ois = new ObjectInputStream(bais);
						try {
							float[][] playerData = (float[][]) ois.readObject();
							System.out.println("Playerdata matrix: ");
							for (int i = 0; i < playerData.length; i++) {
								for (int j = 0; j < playerData[0].length; j++) {
									System.out.println(playerData[i][j]);
								}
							}
							if(firstDraw) {
								firstDraw = false;
								gui.initialDraw(playerData);
							} else {
								gui.update(playerData);
							}
						} catch (ClassNotFoundException e) {
							System.out.println("Could not recreate float matrix");
							e.printStackTrace();
						}
						sendUpdate();

					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	class ConnectionException extends Exception {}

//	public static void main(String args[]) throws UnknownHostException {
//		int port = 8888;
//		try {
//			Client per = new Client(new ClientGUI(), InetAddress.getLocalHost(), port, "Per");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
