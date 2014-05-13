package test.monitoring;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import monitoring.MessageHelper;
import monitoring.command.Command;

import org.junit.Test;

public class MessageHelperTest {

	@Test
	public void test() {
		Command command = MessageHelper.getCommand("create");		
		assertEquals(Command.CREATE, command);
	}
	
	@Test
	public void test2() {
		List<String> strings = new ArrayList<String>();
		strings.add("da");
		strings.add("nu");
		
		List<String> strings2 = new ArrayList<String>(); 
		strings2.addAll(strings);
		
		assertEquals(2,strings.size());
	}
	
	

}
