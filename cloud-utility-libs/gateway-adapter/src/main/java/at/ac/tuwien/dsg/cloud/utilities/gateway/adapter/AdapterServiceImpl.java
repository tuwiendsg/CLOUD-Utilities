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
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Shutdownable;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.CachingProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Provider;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class AdapterServiceImpl implements AdapterService, 
		MessageReceivedListener,  
		Shutdownable {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AdapterServiceImpl.class);
	
	private CachingProducer producer;
	private Consumer consumer;
	private Provider<Message> messageProvider;
	private String generatedChannelName;
	private final ConcurrentHashMap<String, APIResponseObject> registeredAPIs;
	
	private Serializer serializer;
	
	public AdapterServiceImpl(CachingProducer producer,
			Consumer consumer,
			Serializer serializer,
			Provider<Message> messageProvider) {
		this.registeredAPIs = new ConcurrentHashMap<>();
		
		//todo: now that the messaging light rabbit core has been
		//rewriten think about this shit!
		//should we still use a restDiscoveryServiceWrapper
		//or is the functionality now given by the core?
		//consider implementing general reconection legic directly
		//into lightweight messaging ARabbitChannel
		//-> maybe even as own service but most importantly in a common
		//place and then get rid of all the startup blocking nonsence
		//this.discovery = new RestDiscoveryServiceWrapper(settings, this);
		this.producer = producer;
		this.consumer = consumer;
		this.serializer = serializer;
		this.messageProvider = messageProvider;
		this.generatedChannelName = UUID.randomUUID().toString();

		this.consumer.withType(generatedChannelName)
				.addMessageReceivedListener(this);
	}
	
	@Override
	public Adapter createApiAdapter() {
		return new APIObjectAdapter(this);
	}

	@Override
	public void send(APIObject api) throws NoDiscoveryException {	
		try {
			ChannelWrapper wrappedMsg = new ChannelWrapper<APIObject>();
			wrappedMsg.setBody(api);
			wrappedMsg.setResponseChannelName(this.generatedChannelName);

			Message message = this.messageProvider
					.get()
					.withType("registerApiChannel")
					.setMessageKey(api.getTargetUrl())
					.setMessage(this.serializer.serialze(wrappedMsg));

			this.producer.sendMessage(message);
		} catch (IOException ex) {
			this.logger.error("Exception while trying to send API!", ex);
		}
	}

	@Override
	public void delete(String targetUrl) {
		this.producer.removeMessageFromCache(targetUrl);
		
		if (this.registeredAPIs.containsKey(targetUrl)) {
			APIResponseObject api = this.registeredAPIs.get(targetUrl);
			this.deleteApi(api);
			this.registeredAPIs.remove(targetUrl);
		}
	}

	private void deleteApi(APIResponseObject api) {
		try {
			ChannelWrapper<APIResponseObject> wrappedMsg = 
					new ChannelWrapper<>();
			wrappedMsg.setBody(api);

			Message msg = this.messageProvider
					.get()
					.setMessage(this.serializer.serialze(wrappedMsg))
					.withType("deleteApi");

			this.producer.sendMessage(msg);
		} catch (IOException ex) {
			logger.error("Failed to map message!", ex);
		}
	}

	@Override
	public void unregisterAllApis() {
		this.producer.clearCache();
		this.registeredAPIs.values().stream().forEach(api -> {
			this.deleteApi(api);
		});
	}

	@Override
	public void messageReceived(Message message) {
		try {
			APIResponseObject res = this.serializer
					.deserilize(message.getMessage(), APIResponseObject.class);
			if(res.isError()) {
				logger.error("Server responded with error! Msg: {}", 
						res.getErrorMsg());
				return;
			}
			this.registeredAPIs.put(res.getName(), res);
		} catch (IOException ex) {
			logger.error("Failed to read message.", ex);
		}
	}

	@Override
	public void shutdown() {
		//this.discovery.shutdown();
	}

	@Override
	public String getType() {
		return this.generatedChannelName;
	}

	
}
