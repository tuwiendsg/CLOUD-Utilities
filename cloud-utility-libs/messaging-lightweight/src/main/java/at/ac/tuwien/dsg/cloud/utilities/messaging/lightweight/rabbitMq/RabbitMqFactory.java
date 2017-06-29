/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.QueueingConsumer;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqFactory {

	public QueueingConsumer getQueueingConsumer(Channel channel) {
		return new QueueingConsumer(channel);
	}

	public ConnectionFactory getConnectionFactory() {
		return new ConnectionFactory();
	}
}
