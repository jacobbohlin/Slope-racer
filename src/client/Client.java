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
	private DatagramPacket dp;
	private boolean connected = false;
	private int id;
	private ClientGUI gui;
	private String name;
	private int mouseX, mouseY;

	public Client(ClientGUI gui, InetAddress ip, int port, String name) {
		this.gui = gui;
		this.name = name;
		mouseX = 50;
		mouseY = 50;
		dp = new DatagramPacket(new byte[1000], 1000);
		dp.setAddress(ip);
		dp.setPort(port);
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Runner r = new Runner();
		r.start();
		try {
			int i = 0;
			while(!connected && i < 3) {
				establishConnection(ip, port);
				i++;
				Thread.sleep(3000);
			}
		} catch (InterruptedException e) {
			System.out.println("Connection timed-out.");
		}
	}
	
	private void establishConnection(InetAddress ip, int port) {
		byte[] handshake = ("connect;" + name).getBytes();
		DatagramPacket connect = new DatagramPacket(handshake, handshake.length);
		connect.setAddress(ip);
		connect.setPort(port);
		try {
			socket.send(connect);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendUpdate(){
		//TODO
	}

	private class Runner extends Thread {

		@Override
		public void run() {
			byte[] buf = new byte[1000];
			DatagramPacket response = new DatagramPacket(buf, buf.length);
			for (;;) {
				try {
					socket.receive(response);
					
					//This happens while the connection is not yet established and the client is waiting for ACK.
					if(!connected) {
						String s = new String(response.getData(), 0, response.getLength());
						if(s.startsWith("ACK")) {
							id = Integer.parseInt(s.split(";")[1]);
							System.out.println("FEEDBACKZZ");
							connected = true;
						} else {
							System.out.println("Connection DENIED!");
						}
						//End of connection-process.
					} else {
						ByteArrayInputStream bais = new ByteArrayInputStream(buf);
						ObjectInputStream ois = new ObjectInputStream(bais);
						try {
							float[][] playerData = (float[][])ois.readObject();
							System.out.println("Playerdata matrix: ");
							for(int i = 0; i < playerData.length; i++){
								for(int j = 0; j < playerData[0].length; j++){
									System.out.println(playerData[i][j]);
								}
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
	
	public static void main(String args[]) throws UnknownHostException{
		Client per = new Client(new ClientGUI(), InetAddress.getLocalHost(), 8888, "Per");
	}
}
