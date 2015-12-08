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
