package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.collections.ObservableSet;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Client extends Service<ObservableSet<float[][]>> {
	private DatagramSocket socket;
	private boolean connected = false;
	private int id;
	private String name;
	private InetAddress ip;
	private int port;
	private float[][] playerData;
	 
	

	public Client(InetAddress ip, int port, String name)  throws IOException, ConnectionException {
		System.out.println("Creating client");
		this.name = name;
		this.ip = ip;
		this.port = port;
	}

	@Override
	protected Task createTask() {
		System.out.println("Creating Task");
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}		
		Runner r = new Runner();
		r.call();
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
		} catch (InterruptedException | IOException | ConnectionException e) {
			System.out.println("Connection timed-out.");
		}
		return null;
	}
	
	private void establishConnection() throws IOException {
		byte[] handshake = ("connect;" + name).getBytes();
		DatagramPacket connect = new DatagramPacket(handshake, handshake.length);
		connect.setAddress(ip);
		connect.setPort(port);
		socket.send(connect);
		System.out.println("Trying to connect...");

	}
	
	public void sendUpdate(int mouseX, int mouseY) {
		byte[] buf = ("update;" + mouseX + ";" + mouseY).getBytes();		
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
	
	public float[][] getPlayerData() {
		return playerData;
	}
	
	private class Runner extends Task<ObservableSet<float[][]>> {

		@Override
		public ObservableSet<float[][]> call() {
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
							playerData = (float[][]) ois.readObject();
							System.out.println("Playerdata matrix: ");
							for (int i = 0; i < playerData.length; i++) {
								for (int j = 0; j < playerData[0].length; j++) {
									System.out.println(playerData[i][j]);
								}
							}
						} catch (ClassNotFoundException e) {
							System.out.println("Could not recreate float matrix");
							e.printStackTrace();
						}
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
