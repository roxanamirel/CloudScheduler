package monitoring;

import monitoring.command.Command;
import monitoring.types.*;

public class MessageHelper {

	public static Type getType(String type) {
		return Type.valueOf(type.toUpperCase());
	}

	public static Command getCommand(String command) {
		return Command.valueOf(command.toUpperCase());
	}

}
