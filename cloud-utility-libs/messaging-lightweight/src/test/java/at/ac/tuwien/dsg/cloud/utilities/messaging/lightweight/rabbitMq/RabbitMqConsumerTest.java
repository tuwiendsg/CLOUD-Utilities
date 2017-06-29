/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ReceivingChannel;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqConsumerTest {

	private ReceivingChannel channelMock;
	private RabbitMqConsumer subject;
	private boolean testDone;
	private int testCounter;
	private int messageCounter;
	private boolean testFailed;

	@Before
	public void setUp() {
		channelMock = Mockito.mock(ReceivingChannel.class);
		this.subject = new RabbitMqConsumer(channelMock);
		testCounter = 0;
		messageCounter = 0;
		testDone = false;
		testFailed = false;
	}

	@After
	public void tearDown() {
	}

	@Test(timeout = 5000)
	public void testWithDefaultTypeListener() throws InterruptedException {
		RabbitMqMessage msg = new RabbitMqMessage();
		msg.setMessage("{test:test}".getBytes());
		Mockito.when(this.channelMock.getDelivery()).thenAnswer(
				new Answer<RabbitMqMessage>() {
			@Override
			public RabbitMqMessage answer(InvocationOnMock invocation) throws InterruptedException {
				Thread.sleep(500);
				return msg;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				assertEquals(msg, message);
				testDone = true;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				fail("This should not be called!");
			}

			@Override
			public String getType() {
				return "type1";
			}
		});

		while (!testDone) {
			Thread.sleep(100);
		}
	}

	@Test(timeout = 5000)
	public void testWithMultipleTypeListener() throws InterruptedException {
		RabbitMqMessage msg = new RabbitMqMessage();
		msg.setMessage("{test:test}".getBytes());
		msg.withType("type1");
		Mockito.when(this.channelMock.getDelivery()).thenAnswer(
				new Answer<RabbitMqMessage>() {
			@Override
			public RabbitMqMessage answer(InvocationOnMock invocation) throws InterruptedException {
				while(testCounter >= 1) {
					Thread.sleep(1000);
				}
				
				Thread.sleep(500);
				return msg;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				assertEquals(msg, message);
				testCounter++;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				assertEquals(msg, message);
				testCounter++;
			}

			@Override
			public String getType() {
				return "type1";
			}
		});

		while (testCounter != 2) {
			Thread.sleep(100);
		}
	}

	@Test(timeout = 5000)
	public void testRemoveTypeListener() throws InterruptedException {
		RabbitMqMessage msg = new RabbitMqMessage();
		msg.setMessage("{test:test}".getBytes());
		msg.withType("type1");
		Mockito.when(this.channelMock.getDelivery()).thenAnswer(
				new Answer<RabbitMqMessage>() {
			@Override
			public RabbitMqMessage answer(InvocationOnMock invocation) throws InterruptedException {
				while(testCounter >= 1) {
					Thread.sleep(1000);
				}
				
				Thread.sleep(500);
				return msg;
			}
		});
		
		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				assertEquals(msg, message);
				testDone = true;
			}
		});
		
		MessageReceivedListener type1 = new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				fail("This should not be called!");
			}

			@Override
			public String getType() {
				return "type1";
			}
		};

		this.subject.addMessageReceivedListener(type1);
		
		this.subject.removeMessageReceivedListener(type1);

		while (!testDone) {
			Thread.sleep(100);
		}
	}
	
	@Test(timeout = 5000)
	public void testIgnoreDuplicatedMessage() throws InterruptedException {
		RabbitMqMessage msg = new RabbitMqMessage();
		msg.setMessage("{test:test}".getBytes());
		msg.withType("type1");
		msg.withType("type2");
		Mockito.when(this.channelMock.getDelivery()).thenAnswer(
				new Answer<RabbitMqMessage>() {
			@Override
			public RabbitMqMessage answer(InvocationOnMock invocation) throws InterruptedException {
				while(messageCounter >= 2 && testCounter >= 1) {
					Thread.sleep(1000);
				}
				
				Thread.sleep(500);
				messageCounter++;
				return msg;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				if(messageCounter == 2) {
					testFailed = true;
					fail("This should not be called!");
				}
				assertEquals(msg, message);
				testCounter++;
			}
		});

		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {
				if(messageCounter == 2) {
					testFailed = true;
					fail("This should not be called!");
				}
				assertEquals(msg, message);
				testCounter++;
			}

			@Override
			public String getType() {
				return "type1";
			}
		});
		
		this.subject.addMessageReceivedListener(new MessageReceivedListener() {
			@Override
			public void messageReceived(Message message) {	
				if(messageCounter == 2) {
					testFailed = true;
					fail("This should not be called!");
				}
				assertEquals(msg, message);
				testCounter++;
			}

			@Override
			public String getType() {
				return "type2";
			}
		});

		while (testCounter != 3) {
			Thread.sleep(100);
		}
		
		Thread.sleep(1000);
		
		assertFalse(testFailed);
	}
}
