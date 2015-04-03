import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Test class for CS425 Transport layer.
 * @author mmcginnis
 *
 */
public class TestSender {

	public static void main(String[] args) {
		
		CS425Network network2 = new CS425Network();
		
		CS425Transport transport2 = new CS425Transport(network2);
		
		//CS425EndpointInterface receiverEndpoint = transport2.accept("8037");
		
		CS425EndpointInterface senderEndpoint;
		
			senderEndpoint = transport2.connect("127.0.0.1", "8081");
		
		
		
		while(true){
		/*
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			senderEndpoint.send("What?HEHEHEHE");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			senderEndpoint.send("Huh?NONONONONO");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
		//System.out.println(receiverEndpoint.recv());
		senderEndpoint.send("123456789");
			
		}
		
		
	}

}
