import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 
 */

/**
 * @author mmcginnis
 *
 */
public class CS425Network implements CS425NetworkInterface {
	
	private DatagramSocket socket;

	public CS425Network(){
		
	}
	
	@Override
	public int send(DatagramPacket dg) {
		int retval = 0;
		try {
			socket.send(dg);
		} catch (IOException e) {
			e.printStackTrace();
			retval = -1;
		}
		return retval;
	}

	@Override
	public int recv(DatagramPacket dg) {
		int retval = 0;
		try {
			socket.receive(dg);
		} catch (IOException e) {
			e.printStackTrace();
			retval = -1;
		}
		return retval;
	}

}
