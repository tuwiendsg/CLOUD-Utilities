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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ReceivingChannel extends ARabbitChannel {

	private static Logger logger = LoggerFactory.getLogger(ReceivingChannel.class);

	private String queueName;
	private QueueingConsumer consumer;
	private List<String> types;

	public ReceivingChannel(Discovery discovery,
			Serializer serializer,
			RabbitMqFactory rabbitMqFactory) {
		super(discovery, serializer, rabbitMqFactory);
		this.types = new ArrayList<>();
	}

	private void reconnect() {
		if (shutdown.get()) {
			return;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			return;
		}

		this.queueName = null;
		try {
			Channel channel = this.getChannel();

			this.queueName = channel.queueDeclare().getQueue();
			this.consumer = this.rabbitMqFactory.getQueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			synchronized (types) {
				for (String type : types) {
					this.internalBindType(type);
				}
			}
		} catch (ChannelException | IOException | IllegalStateException ex) {
			this.reconnect();
		}
	}

	private void internalBindType(String type) throws IllegalStateException {
		try {
			this.consumer.getChannel().queueBind(queueName,
					ARabbitChannel.EXCHANGE_NAME, type);
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public void bindType(String type) throws IllegalStateException {
		synchronized (types) {
			this.types.add(type);
		}

		if (this.queueName == null) {
			return;
		}

		this.internalBindType(type);
	}

	public RabbitMqMessage getDelivery() {
		if (this.shutdown.get()) {
			return null;
		}

		//this ensures that we start listening for incoming messages
		//when there is really someone who is interested in listening
		//this way we ensure a lazy startup
		if (this.queueName == null) {
			this.reconnect();
		}

		try {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			return this.serializer.deserilize(delivery.getBody(),
					RabbitMqMessage.class);
		} catch (IOException |
				InterruptedException |
				ShutdownSignalException |
				ConsumerCancelledException ex) {
			logger.warn("Error while receiving message!", ex);
			this.reconnect();
		}

		return this.getDelivery();
	}
}
