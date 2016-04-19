/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingHelperFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import javax.inject.Provider;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class GatewayAdapterFactory {

	public static AdapterService adapterServiceImpl(DiscoverySettings settings) {
		Discovery discovery = ComotMessagingFactory
				.getServiceDiscovery(settings);

		Provider<Message> provider = new Provider<Message>() {
			@Override
			public Message get() {
				return ComotMessagingFactory.getRabbitMqMessage();
			}
		};

		return new AdapterServiceImpl(
				ComotMessagingFactory.getCachingRabbitMqProducer(discovery),
				ComotMessagingFactory.getRabbitMqConsumer(discovery),
				ComotMessagingHelperFactory.getJacksonSerializer(),
				provider);
	}
}
