/**
 * 
 */

/**
 * @author mmcginnis
 *
 */
public class CS425Tranport implements CS425TransportInterface {
	
	private class CS425Endpoint implements CS425EndpointInterface{

		@Override
		public int send(String s) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String recv() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int close() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	@Override
	public CS425EndpointInterface connect(String remoteHost,
			String remoteServiceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CS425EndpointInterface accept(String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

}
