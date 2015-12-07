/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.lang.reflect.InvocationTargetException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class AListener<O, T extends Task<ChannelWrapper<O>>> implements MessageReceivedListener {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AListener.class);
	
	protected RegistryService registryService;
	private Consumer consumer;
	private Class<T> taskClazz;
	private Serializer<ChannelWrapper> serializer;
	
	protected AListener(RegistryService service, String channelName, 
			Class<T> taskClazz) {
		
		this.serializer = new JacksonSerializer<ChannelWrapper>(ChannelWrapper.class);
		
		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer(service.getDiscovery())
				.addMessageReceivedListener(this)
				.withType(channelName);
		
		this.registryService = service;
		this.taskClazz = taskClazz;
	}
	
	protected abstract Class<O> getInnerClass();
	
	@Override
	public void messageReceived(Message message) {
		try {			
			ChannelWrapper<O> object = serializer.withInnerType(getInnerClass()).deserilize(message.getMessage());
			T task = taskClazz.getConstructor(RegistryService.class)
					.newInstance(this.registryService);
			task.setBody(object);
			this.registryService.execute(task);
		} catch (IOException 
				| NoSuchMethodException 
				| SecurityException 
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException ex) {
			logger.error("Unexpected error!", ex);
		}
	}
}
