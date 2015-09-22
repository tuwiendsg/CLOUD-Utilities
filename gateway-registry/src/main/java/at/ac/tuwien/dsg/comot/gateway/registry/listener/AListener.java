/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry.listener;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.comot.gateway.registry.ConfigService;
import at.ac.tuwien.dsg.comot.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.comot.gateway.registry.tasks.Task;
import at.ac.tuwien.dsg.comot.messaging.api.Consumer;
import at.ac.tuwien.dsg.comot.messaging.api.Message;
import at.ac.tuwien.dsg.comot.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.comot.messaging.lightweight.ComotMessagingFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class AListener<O, T extends Task<ChannelWrapper<O>>> implements MessageReceivedListener {
	
	protected RegistryService registryService;
	private Consumer consumer;
	private Class<T> taskClazz;
	
	protected AListener(RegistryService service, ConfigService config, 
			String channelName, Class<T> taskClazz) {
		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer()
				.withLightweigthSalsaDiscovery(config.getConfig())
				.addMessageReceivedListener(this)
				.withType(channelName);
		
		this.registryService = service;
		this.taskClazz = taskClazz;
	}
	
	@Override
	public void messageReceived(Message message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ChannelWrapper object = mapper.readValue(message.getMessage(), ChannelWrapper.class);
			
			T task = taskClazz.getConstructor(RegistryService.class)
					.newInstance(this.registryService);
			task.setBody(object);
			this.registryService.execute(task);
		} catch (IOException ex) {
			Logger.getLogger(RegisterListener.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException | SecurityException 
				| InstantiationException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException ex) {
			Logger.getLogger(AListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
