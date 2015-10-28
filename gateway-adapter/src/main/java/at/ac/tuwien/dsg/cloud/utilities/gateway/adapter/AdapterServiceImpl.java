/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class AdapterServiceImpl implements AdapterService, MessageReceivedListener, 
		RestDiscoveryServiceWrapperCallback {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AdapterServiceImpl.class);
	
	private Producer producer;
	private Consumer consumer;
	private Config config;

	private String generatedChannelName;
	private ObjectMapper mapper;
	private RestDiscoveryServiceWrapper discovery;
	
	private boolean cachingMode;
	private final ConcurrentHashMap<String, APIObject> cachedAPIs;
	private final ConcurrentHashMap<String, APIResponseObject> registeredAPIs;

	public AdapterServiceImpl(Config config) {
		this(config, false);
	}
	
	public AdapterServiceImpl(Config config, boolean cachingMode) {
		this.cachingMode = cachingMode;
		this.registeredAPIs = new ConcurrentHashMap<>();
		this.mapper = new ObjectMapper();
		this.cachedAPIs = new ConcurrentHashMap<>();
		this.discovery = new RestDiscoveryServiceWrapper(config, this);
	}
	
	@Override
	public void discoveryIsOnline() {
		this.producer = ComotMessagingFactory
				.getRabbitMqProducer(discovery);

		//todo: fix response idetifier generation
		this.generatedChannelName = "generateTypeIdentifier";

		this.consumer = ComotMessagingFactory
				.getRabbitMqConsumer(discovery)
				.withType(this.generatedChannelName)
				.addMessageReceivedListener(this);
		
		if(this.cachingMode) {
			this.cachedAPIs.values().stream().forEach(api -> {
				try {
					this.send(api);
				} catch (NoDiscoveryException ex) {
					//not possible
				}
			});
			
			this.cachedAPIs.clear();
		}
	}

	@Override
	public Adapter createApiAdapter() {
		return new APIObjectAdapter(this);
	}

	@Override
	public void send(APIObject api) throws NoDiscoveryException {
		if(this.generatedChannelName == null) {
			if(this.cachingMode) {
				this.cachedAPIs.put(api.targetUrl, api);
				return;
			}
			
			throw new NoDiscoveryException("A discovery service could not be "
					+ "found.");
		}
				
		try {
			ChannelWrapper wrappedMsg = new ChannelWrapper<APIObject>();
			wrappedMsg.setBody(api);
			wrappedMsg.setResponseChannelName(this.generatedChannelName);

			//todo: check object and throw exception if neccessary
			ObjectMapper mapper = new ObjectMapper();

			Message message = ComotMessagingFactory
					.getRabbitMqMessage()
					.withType("apiRegistry")
					.setMessage(mapper.writeValueAsBytes(wrappedMsg));

			this.producer.sendMessage(message);
		} catch (IOException ex) {
		}
	}

	@Override
	public void delete(String targetUrl) {
		if(this.cachingMode) {
			if(this.cachedAPIs.containsKey(targetUrl)) {
				this.cachedAPIs.remove(targetUrl);
			}
		}
		
		if (this.registeredAPIs.containsKey(targetUrl)) {
			APIResponseObject api = this.registeredAPIs.get(targetUrl);
			this.deleteApi(api);
			this.registeredAPIs.remove(targetUrl);
		}
	}

	private void deleteApi(APIResponseObject api) {
		try {
			ChannelWrapper<APIResponseObject> wrappedMsg = new ChannelWrapper<>();
			wrappedMsg.setBody(api);

			Message msg = ComotMessagingFactory
					.getRabbitMqMessage()
					.setMessage(mapper.writeValueAsBytes(wrappedMsg))
					.withType("deleteApi");

			this.producer.sendMessage(msg);
		} catch (IOException ex) {
			logger.error("Failed to map message!", ex);
		}
	}

	@Override
	public void unregisterAllApis() {
		if(this.cachingMode) {
			this.cachedAPIs.clear();
		}
		
		this.registeredAPIs.values().stream().forEach(api -> {
			this.deleteApi(api);
		});
	}

	@Override
	public void messageReceived(Message message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			APIResponseObject res = mapper.readValue(message.getMessage(),
					APIResponseObject.class);
			this.registeredAPIs.put(res.getTargetUrl(), res);
		} catch (IOException ex) {
			logger.error("Failed to read message.", ex);
		}
	}

	
}
