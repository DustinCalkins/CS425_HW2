import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Timer;

/**
 * @author mmcginnis
 *
 */
public class CS425Transport implements CS425TransportInterface, ActionListener {
	
	private CS425NetworkInterface network;
	private Timer timer;
	private int DELAY = 10000; //milliseconds
	private int PORT = 8080;
	private int MAX = 1024;
	
	public CS425Transport(CS425NetworkInterface network){
		this.network = network;
		timer = new Timer(DELAY, this);
	}
	
	/**
	 * Action performed for the timer, called after DELAY milliseconds
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	private class CS425Endpoint implements CS425EndpointInterface{

		private InetAddress ip;
		
		public CS425Endpoint(InetAddress ip){
			this.ip = ip;
		}
		
		@Override
		public int send(String s) {
			DatagramPacket dp = new DatagramPacket(s.getBytes(), s.length(), ip, PORT);
			return network.send(dp);
		}

		@Override
		public String recv() {
			byte[] buf = new byte[MAX];  
		    DatagramPacket dp = new DatagramPacket(buf, MAX);  
			network.recv(dp);
			return new String(dp.getData());
		}

		@Override
		public int close() {
			return 0;
		}
	}
	
	

	@Override
	public CS425EndpointInterface connect(String remoteHost,
			String remoteServiceName) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(remoteHost);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return new CS425Endpoint(ip);
	}

	@Override
	public CS425EndpointInterface accept(String serviceName) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return new CS425Endpoint(ip);
	}

}
