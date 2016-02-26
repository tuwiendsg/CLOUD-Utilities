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
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Shutdownable;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryException;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.RabbitMqFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class ARabbitChannel implements Shutdownable {

	protected static final String EXCHANGE_NAME = "comot";

	protected static volatile ConnectionFactory connectionFactory;
	protected static volatile Connection connection;
	protected AtomicBoolean shutdown;
	protected Discovery discovery;
	protected RabbitMqFactory rabbitMqFactory;

	protected Serializer serializer;

	protected ARabbitChannel(Discovery discovery,
			Serializer serializer,
			RabbitMqFactory rabbitMqFactory) {
		this.serializer = serializer;
		this.discovery = discovery;
		this.rabbitMqFactory = rabbitMqFactory;
		connectionFactory = rabbitMqFactory.getConnectionFactory();
		this.shutdown = new AtomicBoolean(false);
	}

	protected Channel getChannel() throws ChannelException {
		try {
			synchronized(connectionFactory) {
				if (connection == null) {
					try {
						String host = discovery.discoverHost();
						
						if(host == null || host.isEmpty()) {
							throw new ChannelException(String.format(
							"Discovery returned invalid host: %s", host));
						}
						
						connectionFactory.setHost(host);
						connection = connectionFactory.newConnection();
					} catch (IOException | DiscoveryException ex) {
						//we do not reconnect in here do to the fact
						//that we are discarding messages which are
						//send during a disconnection phase
						throw new ChannelException(
								"Exception while connecting to RabbitMQ!", ex);
					}
				}
			}

			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			return channel;
		} catch (IOException ex) {
			//in case we can not create a channel
			//try to establish a new connection
			try {
				//try to close old connection never the less
				connection.close();
			} catch (IOException ex1) {
				//ignore
			}

			connection = null;
			return this.getChannel();
		}
	}

	@Override
	public void shutdown() {
		this.shutdown.set(true);
	}
}
