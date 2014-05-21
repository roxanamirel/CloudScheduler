package monitoring;

import javax.jws.WebMethod;
import javax.jws.WebService;

import monitoring.command.Command;
import monitoring.types.ReferenceModelType;

@WebService
public class HookTriggeredWS {

	private QueueProcessor queueProcessor;

	public HookTriggeredWS() {
		queueProcessor = new QueueProcessor();
		queueProcessor.start();
	}

	@WebMethod
	public void addMessageToQueue(String id, String type, String command) {
		ReferenceModelType typeT = MessageHelper.getType(type);
		Command commandC = MessageHelper.getCommand(command);
		Message message = new Message(id, typeT, commandC);
		queueProcessor.addTOQueue(message);
	}
}
