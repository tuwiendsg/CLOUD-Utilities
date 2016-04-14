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
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqConsumer implements Consumer, Runnable {

	private static Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);
	private HashMap<String, List<MessageReceivedListener>> messageListeners;
	private ExecutorService threadPool;
	private ReceivingChannel channel;
	private Queue<String> messageFifo;

	public RabbitMqConsumer(ReceivingChannel channel) {
		this.messageListeners = new HashMap<>();
		this.threadPool = Executors.newFixedThreadPool(1);
		this.channel = channel;
		this.messageFifo = new CircularFifoQueue<>(100);
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
		logger.trace("Received following message: {}",
				new String(msg.getMessage(), StandardCharsets.UTF_8));
		return msg;
	}

	@Override
	public RabbitMqConsumer addMessageReceivedListener(
			MessageReceivedListener listener) {
		List<MessageReceivedListener> list = null;

		synchronized (this.messageListeners) {
			if (this.messageListeners.containsKey(listener.getType())) {
				list = this.messageListeners.get(listener.getType());
			} else {
				list = new ArrayList<>();
				this.messageListeners.put(listener.getType(), list);
				this.withType(listener.getType());
			}

			if (this.messageListeners.size() == 1) {
				this.threadPool.execute(this);
			}
		}

		synchronized (list) {
			list.add(listener);
		}

		return this;
	}

	@Override
	public RabbitMqConsumer removeMessageReceivedListener(
			MessageReceivedListener listener) {
		synchronized (this.messageListeners) {
			if (!this.messageListeners.containsKey(listener.getType())) {
				return this;
			}

			List<MessageReceivedListener> list
					= this.messageListeners.get(listener.getType());
			
			synchronized (list) {
				list.remove(listener);
			}
			
			if (list.isEmpty()) {
				this.messageListeners.remove(listener.getType());
			}
		}
		return this;
	}

	private void fireForType(String type, Message msg) {
		List<MessageReceivedListener> list = null;
		synchronized (this.messageListeners) {
			list = this.messageListeners.get(type);
		}

		if (list != null) {
			synchronized (list) {
				list.forEach(listener -> {
					listener.messageReceived(msg);
				});
			}
		}
	}

	private void fireMessageReceived(Message msg) {
		for (String type : msg.getTypes()) {
			this.fireForType(type, msg);
		}

		this.fireForType(MessageReceivedListener.defaultType, msg);
	}

	@Override
	public void run() {
		while (!this.messageListeners.isEmpty()) {
			Message msg = this.getMessage();
			
			if(this.messageFifo.contains(msg.getMessageKey())) {
				msg = null;
			}
			
			if (msg != null) {
				this.messageFifo.add(msg.getMessageKey());
				this.fireMessageReceived(msg);
			}
		}
	}
}
