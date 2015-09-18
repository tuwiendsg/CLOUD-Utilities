/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.adapter;

import at.ac.tuwien.dsg.comot.messaging.api.Message;
import at.ac.tuwien.dsg.comot.messaging.api.Producer;
import at.ac.tuwien.dsg.comot.messaging.lightweight.ComotMessagingFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class AdapterService {

	private Producer producer;
	private ConcurrentHashMap<Long, APIObject> apiObjects;

	private APIObject getOrCreateObject() {
		synchronized (apiObjects) {
			long currentId = Thread.currentThread().getId();

			if (apiObjects.containsKey(currentId)) {
				return this.apiObjects.get(currentId);
			}

			APIObject object = new APIObject();
			apiObjects.put(currentId, object);
			return object;
		}
	}

	public void send() {
		try {
			APIObject object = this.getOrCreateObject();
			
			//todo: check object and throw exception if neccessary
			
			ObjectMapper mapper = new ObjectMapper();
			
			Message message = ComotMessagingFactory
					.getRabbitMqMessage()
					.withType("apiRegistry")
					.setMessage(mapper.writeValueAsBytes(object));
			
			this.producer.sendMessage(message);
			
			synchronized(apiObjects) {
				this.apiObjects.remove(Thread.currentThread().getId());
			}
		} catch (IOException ex) {
		}
	}
	
	public AdapterService withName(String name) {
		this.getOrCreateObject().setName(name);
		return this;
	}
}
