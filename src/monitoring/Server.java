package monitoring;

import javax.xml.ws.Endpoint;
public class Server {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9898/HookTriggeredWS", new HookTriggeredWS());
        System.out.println("HookTriggeredWS is ready");

	}

}
