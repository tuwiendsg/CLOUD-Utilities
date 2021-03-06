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
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ChannelException;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.SendingChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqProducer implements Producer {

	private static Logger logger = LoggerFactory.getLogger(RabbitMqProducer.class);

	private SendingChannel channel;

	public RabbitMqProducer(SendingChannel channel) {
		this.channel = channel;
	}

	@Override
	public void sendMessage(Message message) {
		if (!(message instanceof RabbitMqMessage)) {
			throw new IllegalArgumentException("Message is not a RabbitMqMessage!");
		}

		RabbitMqMessage msg = (RabbitMqMessage) message;

		logger.trace("Sending following message: {}",
				new String(msg.getMessage(), StandardCharsets.UTF_8));

		for (String type : message.getTypes()) {
			this.channel.sendMessage(type, msg);
		}
	}
}
