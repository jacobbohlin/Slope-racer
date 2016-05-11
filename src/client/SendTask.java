package client;

import java.io.IOException;
import java.net.DatagramPacket;

import javafx.concurrent.Task;

public class SendTask extends Task<Void> {
	private float x,y;
	private int ability;
	
	public SendTask(float x, float y, int ability) {
		this.x = x;
		this.y = y;
		this.ability = ability;
	}
	@Override
	protected Void call() throws Exception {
		byte[] message = ("update;" + x + ";" + y + ";" + ability).getBytes();
		DatagramPacket dp = new DatagramPacket(message, message.length);
		dp.setAddress(ConnectionInfo.getIp());
		dp.setPort(ConnectionInfo.getPort());
		try {
			ConnectionInfo.getSocket().send(dp);
		} catch(IOException e) {
			System.out.println("Failed to send packet...");
			e.printStackTrace();
		}
		return null;
	}

}
 