/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ReceivingChannel;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.SendingChannel;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.JacksonSerializer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class ComotMessagingHelperFactory {
	
	public static RabbitMqFactory getRabbitMqFactory() {
		return new RabbitMqFactory();
	}
	
	public static Serializer getJacksonSerializer() {
		return new JacksonSerializer();
	}
	
	public static SendingChannel getSendingChannel(Discovery discovery,
			Serializer serializer,
			RabbitMqFactory rabbitMqFactory) {
		return new SendingChannel(discovery, serializer, rabbitMqFactory);
	}
	
	public static ReceivingChannel getReceivingChannel(Discovery discovery,
			Serializer serializer,
			RabbitMqFactory rabbitMqFactory) {
		return new ReceivingChannel(discovery, serializer, rabbitMqFactory);
	}
}
