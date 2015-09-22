/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.adapter;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.comot.gateway.adapter.model.ChannelWrapper;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class AdapterService implements MessageReceivedListener {

	private Producer producer;
	private Consumer consumer;
	private Config config;

	private String generatedChannelName;

	private final ConcurrentHashMap<Long, APIObject> apiObjects;
	private List<APIResponseObject> registeredAPIs;

	public AdapterService(Config config) {
		this.producer = ComotMessagingFactory
				.getRabbitMqProducer()
				.withLightweightDiscovery(config);

		//todo: fix response idetifier generation
		this.generatedChannelName = "generateTypeIdentifier";

		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer()
				.withLightweigthSalsaDiscovery(config)
				.withType(this.generatedChannelName)
				.addMessageReceivedListener(this);

		this.apiObjects = new ConcurrentHashMap<>();
		this.registeredAPIs = new ArrayList<>();
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
			ChannelWrapper wrappedMsg = new ChannelWrapper<APIObject>();
			wrappedMsg.setBody(object);
			wrappedMsg.setResponseChannelName(this.generatedChannelName);

			//todo: check object and throw exception if neccessary
			ObjectMapper mapper = new ObjectMapper();

			Message message = ComotMessagingFactory
					.getRabbitMqMessage()
					.withType("apiRegistry")
					.setMessage(mapper.writeValueAsBytes(wrappedMsg));

			this.producer.sendMessage(message);

			synchronized (apiObjects) {
				this.apiObjects.remove(Thread.currentThread().getId());
			}
		} catch (IOException ex) {
		}
	}
	
	public void unregisterAllApis() {
		ObjectMapper mapper = new ObjectMapper();
		
		this.registeredAPIs.stream().forEach(api -> {
			try {
				ChannelWrapper<APIResponseObject> wrappedMsg = new ChannelWrapper<>();
				wrappedMsg.setBody(api);
				
				Message msg = ComotMessagingFactory
						.getRabbitMqMessage()
						.setMessage(mapper.writeValueAsBytes(wrappedMsg))
						.withType("deleteApi");
				
				this.producer.sendMessage(msg);
						} catch (IOException ex) {
				Logger.getLogger(AdapterService.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			this.registeredAPIs.add(mapper.readValue(message.getMessage(),
					APIResponseObject.class));
		} catch (IOException ex) {
			Logger.getLogger(AdapterService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
