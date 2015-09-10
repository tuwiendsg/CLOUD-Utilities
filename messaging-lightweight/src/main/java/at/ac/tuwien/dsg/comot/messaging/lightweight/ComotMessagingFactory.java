/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.messaging.lightweight;

import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.factory.RabbitMqConsumerFactory;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.factory.RabbitMqProducerFactory;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.RabbitMqMessage;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingFactory {
	public static RabbitMqConsumerFactory getRabbitMqConsumer() {
		return new RabbitMqConsumerFactory();
	}
	
	public static RabbitMqProducerFactory getRabbitMqProducer() {
		return new RabbitMqProducerFactory();
	}
	
	public static RabbitMqMessage getRabbitMqMessage() {
		return new RabbitMqMessage();
	}
}
