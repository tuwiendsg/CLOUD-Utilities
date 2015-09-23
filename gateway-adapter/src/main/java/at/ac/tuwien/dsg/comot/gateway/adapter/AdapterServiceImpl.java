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
public class AdapterServiceImpl implements AdapterService, MessageReceivedListener {

	private Producer producer;
	private Consumer consumer;
	private Config config;

	private String generatedChannelName;
	private ObjectMapper mapper;

	//todo: make here a map between api name and responseObject
	private final ConcurrentHashMap<String, APIResponseObject> registeredAPIs;

	public AdapterServiceImpl(Config config) {
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

		this.registeredAPIs = new ConcurrentHashMap<>();
		this.mapper = new ObjectMapper();
	}

	public Adapter createApiAdapter() {
		return new APIObjectAdapter(this);
	}

	public void send(APIObject api) {
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

	public void delete(String targetUrl) {
		if (this.registeredAPIs.containsKey(targetUrl)) {
			APIResponseObject api = this.registeredAPIs.get(targetUrl);
			this.deleteApi(api);
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
			Logger.getLogger(AdapterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void unregisterAllApis() {
		this.registeredAPIs.values().stream().forEach(api -> {
			this.deleteApi(api);
		});
	}

	//todo: hide messageReceived behind an AdapterInterface
	@Override
	public void messageReceived(Message message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			APIResponseObject res = mapper.readValue(message.getMessage(),
					APIResponseObject.class);
			this.registeredAPIs.put(res.getTargetUrl(), res);
		} catch (IOException ex) {
			Logger.getLogger(AdapterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
