/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ChannelException;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class MapCachingProducerTest {
	
	@Test
	public void testSendMessage() {
		
		Message message = new RabbitMqMessage();
		message.withMessageKey("myKey");
		HashMap<String, Message> actual = new HashMap<>();
		
		Producer producerMock = mock(Producer.class);
		doThrow(new ChannelException())
				.when(producerMock)
				.sendMessage(message);
		
		MapCachingProducer subject = 
				new MapCachingProducer(producerMock, actual);
		
		subject.sendMessage(message);
		
		assertEquals(1, actual.size());
		assertEquals(message, actual.get("myKey"));
	}
	
	@Test
	public void testSimulateSendMessage() {
		
		Message message = new RabbitMqMessage();
		message.withMessageKey("myKey");
		HashMap<String, Message> actual = new HashMap<>();
		actual.put("myKey", message);

		Producer producerMock = mock(Producer.class);
		
		MapCachingProducer subject = 
				new MapCachingProducer(producerMock, actual);
		
		subject.run();
		
		assertTrue(actual.isEmpty());
	}
}
