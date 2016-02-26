/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.SendingChannel;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqProducerTest {
	
	private SendingChannel channelMock;
	private RabbitMqProducer subject;
	private boolean testDone;
	private int testCounter;
	
	@Before
	public void setUp() {
		this.channelMock = mock(SendingChannel.class);
		subject = new RabbitMqProducer(channelMock);
	}

	@Test
	public void testSendMessage() {
		RabbitMqMessage msg = new RabbitMqMessage();
		msg.setMessage("test".getBytes());
		msg.withType("type1").withType("type2");
		
		subject.sendMessage(msg);
		
		verify(channelMock, times(1)).sendMessage("type1", msg);
		verify(channelMock, times(1)).sendMessage("type2", msg);
	}
	
}
