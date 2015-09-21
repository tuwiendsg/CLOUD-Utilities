/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.adapter;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.messaging.api.Consumer;
import at.ac.tuwien.dsg.comot.messaging.api.Message;
import at.ac.tuwien.dsg.comot.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.comot.messaging.api.Producer;
import at.ac.tuwien.dsg.comot.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class AdapterService implements MessageReceivedListener {

	private Producer producer;
	private Consumer consumer;
	
	private ConcurrentHashMap<Long, APIObject> apiObjects;
	private List<String> apiIds;
	
	public AdapterService(Config config) {
		this.producer = ComotMessagingFactory
				.getRabbitMqProducer()
				.withLightweightDiscovery(config);
		
		//todo: fix response idetifier generation
		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer()
				.withLightweigthSalsaDiscovery(config)
				.withType("generateTypeIdentifier")
				.addMessageReceivedListener(this);
		
		this.apiObjects = new ConcurrentHashMap<>();
		this.apiIds = new ArrayList<>();
	}

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
	
	public AdapterService withPublicDns(String dns) {
		this.getOrCreateObject().setPublicDns(dns);
		return this;
	}
	
	public AdapterService withPath(String path) {
		this.getOrCreateObject().setPath(path);
		return this;
	}
	
	public AdapterService doStripPath(boolean stripPath) {
		this.getOrCreateObject().setStripPath(stripPath);
		return this;
	}
	
	public AdapterService doPreserveHost(boolean preserveHost) {
		this.getOrCreateObject().setPreserveHost(preserveHost);
		return this;
	}
	
	public AdapterService withTargetUrl(String targetUrl) {
		this.getOrCreateObject().setTargetUrl(targetUrl);
		return this;
	}

	@Override
	public void messageReceived(Message message) {
		
	}
}
