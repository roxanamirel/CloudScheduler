package monitoring;

import monitoring.command.Command;
import monitoring.types.*;

public class MessageHelper {

	public static ReferenceModelType getType(String type) {
		return ReferenceModelType.valueOf(type.toUpperCase());
	}

	public static Command getCommand(String command) {
		return Command.valueOf(command.toUpperCase());
	}

}
