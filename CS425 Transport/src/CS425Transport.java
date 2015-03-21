import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.Timer;

/**
 * @author mmcginnis
 *
 */
public class CS425Transport implements CS425TransportInterface, ActionListener {

	private CS425NetworkInterface network;
	private Timer timer;
	private int DELAY = 5000; // milliseconds
	private int MAX = 520; // 512 Max String, 8 ByteHeader

	public CS425Transport(CS425NetworkInterface network) {
		this.network = network;
		timer = new Timer(DELAY, this);
	}

	/**
	 * Action performed for the timer, called after DELAY milliseconds
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	}

	/*
	 * Packet Header
	 * 
	 * ______________________________________________________
	 * 
	 * |-----------------32-bit-Hash-Value------------------| // HashValue/Int
	 * of the Transports Choosing, if zero close connection
	 * 
	 * |--15-bit-Length--|--2-bit-Speed--|--15-bit-LastAck--| // Total Length of
	 * Data, Speed Controller see below, Last Acknowledgement Recieved // 512 is
	 * Maximum size of string in each packet.
	 * |----------------String-Data-As-Bytes----------------|
	 * 
	 * ...
	 * 
	 * ... ______________________________________________________
	 * 
	 * 
	 * Speed Truth Table | 0 | 0 | Do nothing | | 0 | 1 | Slow Down | | 1 | 0 |
	 * Speed Up | | 1 | 1 | Stop/Wait |
	 * 
	 * @author mmcginnis
	 */
	private class CS425Endpoint implements CS425EndpointInterface {

		private InetAddress ip;
		private int hashValue; // 32 bit
		private short lastAck = -1; // 16 bit

		public CS425Endpoint(InetAddress ip, int hashValue) {
			this.ip = ip;
			this.hashValue = hashValue;
		}

		@Override
		public int send(String s) {
			byte[] b = makeHeaderSendPackets(s);
			DatagramPacket dp = new DatagramPacket(s.getBytes(), s.length(),
					ip, hashValue);
			if (network.send(dp) < 0)
				return -1;
			return b.length;
		}

		private byte[] makeHeaderSendPackets(String s) {
			byte[] hashBytes = BE_IntToByte(hashValue);
			int length = s.length(); // 32 bits for second row, length of string
			int l = length;
			l <<= 17; // shift room for speed and last ack
			l |= ++lastAck; // put ++lastAck in bottom bits
			// Speed not included
			byte[] rowBytes = BE_IntToByte(l);
			int i = 0;
			byte[] packet = new byte[MAX + 8];
			byte[] string = s.getBytes();

			// While sending if any network.send is less than zero return -1
			while (length + 8 /* Size of Byte Arrays */>= MAX) { // Break into
																// Multiple
																// Packets
				// Divide into Packets
			}
			// Send Single Remaining Packet
			// Whatever is left of string send that.

			return null;
		}

		private String readHeader(byte[] buf) {
			byte[] headerBytes = new byte[8];
			for (int i = 0; i < 8; i++)
				headerBytes[i] = buf[i];
			int tmpHash = BE_HashByteToHash(headerBytes);
			if (tmpHash != hashValue) // Packet has been corrupted.. or
										// something don't bother.
				return null;

			// read the rest of the header

			// while more packets build string

			// return the string after all acks have been recieved
			return null;
		}

		public byte[] BE_IntToByte(int myInteger) {
			return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
					.putInt(myInteger).array();
		}

		private int BE_HashByteToHash(byte[] bytes) {
			byte[] b = new byte[4];
			for (int i = 0; i < 4; i++)
				b[i] = bytes[i];
			return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getInt();
		}

		@Override
		public String recv() {
			byte[] buf = new byte[MAX];
			DatagramPacket dp = new DatagramPacket(buf, MAX);
			network.recv(dp);
			return readHeader(dp.getData());
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

		return new CS425Endpoint(ip, remoteServiceName.hashCode());
	}

	@Override
	public CS425EndpointInterface accept(String serviceName) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return new CS425Endpoint(ip, 0);
	}

}
