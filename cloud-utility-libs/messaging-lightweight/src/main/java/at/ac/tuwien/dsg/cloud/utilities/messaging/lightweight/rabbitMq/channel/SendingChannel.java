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
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqMessage;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class SendingChannel extends ARabbitChannel {
	private static Logger logger = LoggerFactory.getLogger(SendingChannel.class);
	
	public SendingChannel(Discovery discovery, 
			Serializer serializer,
			RabbitMqFactory rabbitMqFactory) {
		super(discovery, serializer, rabbitMqFactory);
	}

	public void sendMessage(String type, RabbitMqMessage msg) throws ChannelException {
		try {
			byte[] body = this.serializer.serialze(msg);
			Channel channel = this.getChannel();
			channel.basicPublish(ARabbitChannel.EXCHANGE_NAME, type, null, body);
			channel.close();
		} catch (IOException ex) {
			logger.error(String
					.format("Error while sending message with type %s!", type)
					, ex);
		}
	}
}
