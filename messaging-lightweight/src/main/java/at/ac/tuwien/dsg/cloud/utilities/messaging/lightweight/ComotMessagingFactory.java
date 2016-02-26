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
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.RestServiceDiscovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqConsumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingFactory {

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

	public static Discovery getRestServiceDiscovery(DiscoverySettings settings) {
		return new RestServiceDiscovery(settings,
				ComotMessagingHelperFactory.getJacksonSerializer());
	}
}
