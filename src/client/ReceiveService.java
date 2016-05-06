package client;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ReceiveService extends Service<Void>{
	private float[][] playerData;
	
	@Override
	protected Task<Void> createTask() {
		Task<Void> t = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for(;;) {
					System.out.println();//TA INTE BORT!!!
					DatagramPacket dp = new DatagramPacket(new byte[1000], 1000);
					ConnectionInfo.getSocket().receive(dp);
//					String s = new String(dp.getData(), 0, dp.getLength());
					ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
					ObjectInputStream ois = new ObjectInputStream(bais);
					try {
						playerData = (float[][]) ois.readObject();
						ConnectionInfo.setPlayerData(playerData);
						ConnectionInfo.setFirstPacketReceived();
//						System.out.println("Playerdata matrix: ");
//						for (int i = 0; i < playerData.length; i++) {
//							for (int j = 0; j < playerData[0].length; j++) {
//								System.out.println(playerData[i][j]);
//							}
//						}
					} catch (ClassNotFoundException e) {
						System.out.println("Could not recreate float matrix");
						e.printStackTrace();
					}
				}
			}
		};
		return t;
	}
}
