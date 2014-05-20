package test.monitoring;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
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
	
	@Test
	public void test3() {
		float min = 20;
		
		float a = min / 100 * 0.12345f;		
		float b = (float) min / 100 * 0.12345f;
		
		System.out.println(a);
		System.out.println(b);
		
		assertTrue(a == b);
	}
	
	@Test
	public void test4() {
		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			strings.add(String.valueOf(i));
		}
		
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals("2")) {
				iterator.remove();
			}
		}		
		
		assertEquals(4, strings.size());
	}
	
	

}
