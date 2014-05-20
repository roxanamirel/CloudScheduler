package monitoring;

import monitoring.command.*;
import monitoring.types.*;

public class Message {

	private String id;
	private ReferenceModelType type;
	private Command command;
	
	public Message(String id, ReferenceModelType type, Command command) {
		this.id = id;
		this.type = type;
		this.command = command;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public ReferenceModelType getType() {
		return type;
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [id=" + id + ", type=" + type + ", command=" + command
				+ "]";
	}
}
