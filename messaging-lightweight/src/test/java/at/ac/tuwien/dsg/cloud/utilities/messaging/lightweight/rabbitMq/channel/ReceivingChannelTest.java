/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ReceivingChannelTest {
	@Test
	public void testLazyBindType() throws Exception {
		Discovery discovery = Mockito.mock(Discovery.class);
		Serializer serializer = Mockito.mock(Serializer.class);
		RabbitMqFactory rabbitMqFactory = Mockito.mock(RabbitMqFactory.class);
		
		ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
		Connection connection = Mockito.mock(Connection.class);
		Channel channel = Mockito.mock(Channel.class);
		DeclareOk declareOk = Mockito.mock(DeclareOk.class);
		QueueingConsumer consumer = Mockito.mock(QueueingConsumer.class);
		Delivery delivery = Mockito.mock(Delivery.class);
		RabbitMqMessage msg = Mockito.mock(RabbitMqMessage.class);
		
		String expectedQueue = "testQueue";
		
		Mockito.when(discovery.discoverHost()).thenReturn("localhost");
		Mockito.when(rabbitMqFactory.getConnectionFactory())
				.thenReturn(factory);
		Mockito.when(factory.newConnection()).thenReturn(connection);
		Mockito.when(rabbitMqFactory.getQueueingConsumer(channel))
				.thenReturn(consumer);
		Mockito.when(connection.createChannel()).thenReturn(channel);
		Mockito.when(channel.queueDeclare()).thenReturn(declareOk);
		Mockito.when(declareOk.getQueue()).thenReturn(expectedQueue);
		Mockito.when(consumer.getChannel()).thenReturn(channel);
		Mockito.when(consumer.nextDelivery()).thenReturn(delivery);
		Mockito.when(delivery.getBody()).thenReturn("test".getBytes());
		Mockito
				.when(serializer
						.deserilize("test".getBytes(), RabbitMqMessage.class))
				.thenReturn(msg);
		
		ReceivingChannel subject = new ReceivingChannel(discovery, 
				serializer, 
				rabbitMqFactory);
		
		String expectedRoutingKey1 = "testType1";
		String expectedRoutingKey2 = "testType2";
		
		subject.bindType(expectedRoutingKey1);
		subject.bindType(expectedRoutingKey2);
		
		//due to lazy startup binding should not yet have been triggered
		Mockito.verify(channel, Mockito.never()).queueBind(expectedQueue, 
				ARabbitChannel.EXCHANGE_NAME, expectedRoutingKey1);
		
		Mockito.verify(channel, Mockito.never()).queueBind(expectedQueue, 
				ARabbitChannel.EXCHANGE_NAME, expectedRoutingKey2);
		
		//this call should trigger the binding to the queues
		RabbitMqMessage msgActual = subject.getDelivery();
		
		Mockito.verify(channel, Mockito.times(1)).queueBind(expectedQueue, 
				ARabbitChannel.EXCHANGE_NAME, expectedRoutingKey1);
		
		Mockito.verify(channel, Mockito.times(1)).queueBind(expectedQueue, 
				ARabbitChannel.EXCHANGE_NAME, expectedRoutingKey2);
		
		Assert.assertEquals(msg, msgActual);
	}
}
