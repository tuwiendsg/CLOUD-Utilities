/* 
 * Copyright 2015 Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.CachingProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DirectDiscovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.RestServiceDiscovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqConsumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingFactory {
	
	private static Logger logger = 
			LoggerFactory.getLogger(ComotMessagingFactory.class);

	public static RabbitMqConsumer getRabbitMqConsumer(Discovery discovery) {
		Serializer serializer = ComotMessagingHelperFactory
				.getJacksonSerializer();
		RabbitMqFactory rabbitMqFactory = ComotMessagingHelperFactory
				.getRabbitMqFactory();

		return new RabbitMqConsumer(ComotMessagingHelperFactory
				.getReceivingChannel(discovery, serializer, rabbitMqFactory));
	}

	public static RabbitMqProducer getRabbitMqProducer(Discovery discovery) {
		Serializer serializer = ComotMessagingHelperFactory
				.getJacksonSerializer();
		RabbitMqFactory rabbitMqFactory = ComotMessagingHelperFactory
				.getRabbitMqFactory();

		return new RabbitMqProducer(ComotMessagingHelperFactory
				.getSendingChannel(discovery, serializer, rabbitMqFactory));
	}

	public static CachingProducer getCachingRabbitMqProducer(Discovery discovery) {
		return new MapCachingProducer(
				ComotMessagingFactory.getRabbitMqProducer(discovery));
	}

	public static RabbitMqMessage getRabbitMqMessage() {
		return new RabbitMqMessage();
	}
	
	public static Discovery getServiceDiscovery(DiscoverySettings settings) {
		return getServiceDiscovery(settings, 
				ComotMessagingHelperFactory.getJacksonSerializer());
	}

	public static Discovery getServiceDiscovery(DiscoverySettings settings,
			Serializer serializer) {
		if (settings.getServiceName() == null) {
			logger.info("No discovery service name configured. "
					+ "Wireing direct discovery!");
			return new DirectDiscovery(settings);
		}
		
		return new RestServiceDiscovery(settings, serializer);
	}
}
