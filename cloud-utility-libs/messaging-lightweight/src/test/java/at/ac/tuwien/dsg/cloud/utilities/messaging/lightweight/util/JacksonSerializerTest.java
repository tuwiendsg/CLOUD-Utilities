/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class JacksonSerializerTest {
	
	public JacksonSerializerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testSerialzeDesirializeSimpleObject() throws Exception {
		TestTarget expected = new TestTarget();
		expected.setTestField("test");
		
		JacksonSerializer subject = new JacksonSerializer();
		byte[] bytes = subject.serialze(expected);
		
		TestTarget actual = subject.deserilize(bytes, TestTarget.class);
		
		assertEquals(expected.getTestField(), actual.getTestField());
	}

	@Test
	public void testSerialzeDesirializeComplexObject() throws Exception {
		List<TestTarget> expected = new ArrayList<>();
		TestTarget expected1 = new TestTarget();
		expected1.setTestField("test1");
		TestTarget expected2 = new TestTarget();
		expected2.setTestField("test2");
		expected.add(expected1);
		expected.add(expected2);
		
		JacksonSerializer subject = new JacksonSerializer();
		byte[] bytes = subject.serialze(expected);
		
		List<TestTarget> actual = subject
				.deserilize(bytes, ArrayList.class, TestTarget.class);
		
		assertEquals(2, actual.size());
		assertTrue(actual.stream().anyMatch(item -> 
				item.getTestField().equals(expected1.getTestField())));
		assertTrue(actual.stream().anyMatch(item -> 
				item.getTestField().equals(expected2.getTestField())));
	}	
}
