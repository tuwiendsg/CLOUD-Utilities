/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.messaging.lightweight;

import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.RabbitMqConsumer;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.channel.ReceivingChannel;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.discovery.LightweightSalsaDiscovery;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqConsumerFactory {
	
	public RabbitMqConsumer withLightweigthDiscovery(Config config) {
		return new RabbitMqConsumer(new ReceivingChannel(new LightweightSalsaDiscovery(config)));
	}
}
