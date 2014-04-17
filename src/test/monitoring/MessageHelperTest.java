package test.monitoring;

import static org.junit.Assert.*;

import monitoring.MessageHelper;
import monitoring.command.Command;

import org.junit.Test;

public class MessageHelperTest {

	@Test
	public void test() {
		Command command = MessageHelper.getCommand("create");		
		assertEquals(Command.CREATE, command);
	}

}
