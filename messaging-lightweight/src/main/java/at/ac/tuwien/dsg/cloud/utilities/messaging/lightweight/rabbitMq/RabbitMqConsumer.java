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

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ReceivingChannel;
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
public class RabbitMqConsumer implements Consumer, Runnable {	
	private static Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);
	private List<MessageReceivedListener> messageListeners;
	private ExecutorService threadPool;
	private ReceivingChannel channel;

	public RabbitMqConsumer(ReceivingChannel channel) {
		this.messageListeners = new ArrayList<>();
		this.threadPool = Executors.newFixedThreadPool(1);
		this.channel = channel;
	}

	@Override
	public RabbitMqConsumer withType(String type) {
		this.channel.bindType(type);
		logger.trace("Started listening to {}.", type);
		return this;
	}

	@Override
	public Message getMessage() {
		Message msg = this.channel.getDelivery();
		logger.trace("Received following message: {}", new String(msg.getMessage(), StandardCharsets.UTF_8));
		return msg;
	}

	@Override
	public synchronized RabbitMqConsumer addMessageReceivedListener(MessageReceivedListener listener) {
		this.messageListeners.add(listener);

		if (this.messageListeners.size() == 1) {
			this.threadPool.execute(this);
		}
		
		return this;
	}

	@Override
	public synchronized RabbitMqConsumer removeMessageReceivedListener(MessageReceivedListener listener) {
		this.messageListeners.remove(listener);
		return this;
	}

	private synchronized void fireMessageReceived(Message msg) {
		this.messageListeners.stream().forEach(listener -> {
			listener.messageReceived(msg);
		});
	}

	@Override
	public void run() {
		while (!this.messageListeners.isEmpty()) {
			Message msg = this.getMessage();

			if (msg != null) {
				this.fireMessageReceived(msg);
			}
		}
	}
}
