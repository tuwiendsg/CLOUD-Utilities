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

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.ServerCluster;


/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingService {
	
	private Discovery discovery;
	private ServerCluster cluster;
	
	public ComotMessagingService(Discovery discovery) {
		this.discovery = discovery;
	}
	
	public ComotMessagingService(Discovery discovery, ServerCluster cluster) {
		this.cluster = cluster;
		this.cluster.deploy();
		this.discovery = discovery;
	}
	
	public ServerCluster getServerCluster() {
		return this.cluster;
	}
	
	public Message getRabbitMqMessage() {
		return ComotMessagingFactory.getRabbitMqMessage();
	}
	
	public Consumer getRabbitMqConsumer() {
		return ComotMessagingFactory.getRabbitMqConsumer(discovery);
	}
	
	public Producer getRabbitMqProducer() {
		return ComotMessagingFactory.getRabbitMqProducer(discovery);
	}	
}
