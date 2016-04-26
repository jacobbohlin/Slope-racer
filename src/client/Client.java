package client;

import java.io.IOException;
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

	public Client(ClientGUI gui, InetAddress ip, int port, String name) {
		this.gui = gui;
		this.name = name;
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
				wait(3000);
				i++;
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

	private class Runner extends Thread {

		@Override
		public void run() {
			DatagramPacket response = new DatagramPacket(new byte[1000], 1000);
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
					}
					//End of connection-process.
					
					
					
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
