/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.factory;

import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.RabbitMqProducer;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.channel.SendingChannel;
import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.discovery.LightweightSalsaDiscovery;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqProducerFactory {
	public RabbitMqProducer withLightweightDiscovery(Config config) {
		return new RabbitMqProducer(new SendingChannel(new LightweightSalsaDiscovery(config)));
	}
}
