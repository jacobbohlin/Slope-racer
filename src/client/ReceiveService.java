package client;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ReceiveService extends Service<Void> {
	private float[][] playerData;

	@Override
	protected Task<Void> createTask() {
		Task<Void> t = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (;;) {
					DatagramPacket dp = new DatagramPacket(new byte[1000], 1000);
					try {
						ConnectionInfo.getSocket().receive(dp);
					} catch (SocketTimeoutException e) {
						continue;
					}
					try {
						ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
						ObjectInputStream ois = new ObjectInputStream(bais);
						playerData = (float[][]) ois.readObject();
						ConnectionInfo.setPlayerData(playerData);
						ConnectionInfo.setFirstPacketReceived();
//						System.out.println("Playerdata matrix: ");
//						for (int i = 0; i < playerData.length; i++) {
//							for (int j = 0; j < playerData[0].length; j++) {
//								System.out.println(playerData[i][j]);
//							}
//						}
					} catch (Exception e) {
						System.out.println("Start packet received.");
						byte[] data = dp.getData();
						String message = new String(data, 0, data.length);
						if (message.startsWith("score;")) {
							String[] score = message.substring(6).split(";");
							ConnectionInfo.setScore(score);
						} else {
							String subMessage = (new String(data, 0, data.length)).substring(6);
							System.out.println(subMessage);
							if (subMessage.contains(";")) {
								ConnectionInfo.setPlayerNames(subMessage.split(";"));
							} else {
								String[] names = new String[1];
								names[0] = subMessage;
								ConnectionInfo.setPlayerNames(names);
							}
							System.out.println(ConnectionInfo.getPlayerNames()[0]);
						}
					}
				}
			}
		};
		return t;
	}
}
