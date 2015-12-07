/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqConsumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ReceivingChannel;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.SendingChannel;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.JacksonSerializer;



/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingFactory {
	public static RabbitMqConsumer getRabbitMqConsumer(Discovery discovery) {
		return new RabbitMqConsumer(new ReceivingChannel(discovery, new JacksonSerializer(RabbitMqMessage.class)));
	}
	
	public static RabbitMqProducer getRabbitMqProducer(Discovery discovery) {
		return new RabbitMqProducer(new SendingChannel(discovery, new JacksonSerializer(RabbitMqMessage.class)));
	}
	
	public static RabbitMqMessage getRabbitMqMessage() {
		return new RabbitMqMessage();
	}
}
