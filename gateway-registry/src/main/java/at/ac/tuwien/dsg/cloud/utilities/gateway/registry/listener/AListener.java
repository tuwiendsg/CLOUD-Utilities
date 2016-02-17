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
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.Task;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.JacksonSerializer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import java.io.IOException;
import javax.inject.Provider;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 * @param <O>
 * @param <T>
 */
public abstract class AListener<O, T extends Task<ChannelWrapper<O>>> 
		implements MessageReceivedListener {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AListener.class);
	
	protected RegistryService registryService;
	private final Consumer consumer;
	private final Serializer<ChannelWrapper> serializer;
	private final Provider<T> taskProvider;
	
	protected AListener(RegistryService service, String channelName, 
			Provider<T> taskProvider) {
		
		this.serializer = new JacksonSerializer<>(ChannelWrapper.class);
		
		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer(service.getDiscovery())
				.addMessageReceivedListener(this)
				.withType(channelName);
		
		this.registryService = service;
		this.taskProvider = taskProvider;
	}
	
	protected abstract Class<O> getInnerClass();
	
	@Override
	public void messageReceived(Message message) {
		try {			
			ChannelWrapper<O> object = serializer
					.withInnerType(getInnerClass())
					.deserilize(message.getMessage());
			//T task = this.get();
			T task = this.taskProvider.get();
			task.setBody(object);
			this.registryService.execute(task);
		} catch (IOException | IllegalArgumentException ex) {
			logger.error("Unexpected error!", ex);
		}
	}
}
