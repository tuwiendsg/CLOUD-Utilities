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
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
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
	
	private final ExecutorService executorService;
	private final Serializer serializer;
	private final Provider<T> taskProvider;
	
	protected AListener(ExecutorService service, 
			Provider<T> taskProvider, 
			Serializer serializer) {
		
		this.serializer = serializer;
		this.executorService = service;
		this.taskProvider = taskProvider;
	}
	
	protected abstract Class<O> getInnerClass();
	
	@Override
	public void messageReceived(Message message) {
		try {			
			ChannelWrapper<O> object = serializer
					.deserilize(message.getMessage(), ChannelWrapper.class,
							getInnerClass());
			T task = this.taskProvider.get();
			task.setBody(object);
			this.executorService.execute(task);
		} catch (IOException | IllegalArgumentException ex) {
			logger.error("Unexpected error!", ex);
		}
	}
}
