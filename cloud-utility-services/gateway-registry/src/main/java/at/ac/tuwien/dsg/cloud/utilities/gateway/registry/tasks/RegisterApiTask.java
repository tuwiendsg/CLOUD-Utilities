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
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.KongService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongPlugin;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.DecisionSettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RegisterApiTask extends ATask<ChannelWrapper<APIObject>> {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterApiTask.class);
	
	private final KongService kongService;
	private final Producer producer;
	private List<KongPlugin> plugins = new ArrayList<>();
	
	public RegisterApiTask(KongService service, Producer producer, 
			DecisionSettings decisionSettings) {
		this.kongService = service;
		this.producer = producer;
		
		KongPlugin authPlugin = new KongPlugin();
		authPlugin.setName("key-auth");
		KongPlugin restDecisionPlugin = new KongPlugin();
		restDecisionPlugin.setName("restDecisionPlugin");
		restDecisionPlugin.addConfigProperty("url", decisionSettings.asUrl());
		
		plugins.add(authPlugin);
		plugins.add(restDecisionPlugin);
	}

	@Override
	public void run() {
		try {
			KongApiObject apiObject = KongApiObject
					.fromApiObject(this.wrapper.getBody());
			KongApiResponseObject resp = this.kongService.registerApi(apiObject);
			
			if(!resp.isError()) {
				for(KongPlugin p: this.plugins) {
					this.kongService.enablePlugin(apiObject, p);
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			
			Message response = ComotMessagingFactory
					.getRabbitMqMessage()
					.withType(this.wrapper.getResponseChannelName())
					.setMessage(mapper
							.writeValueAsBytes((APIResponseObject)resp));
			
			this.producer.sendMessage(response);
		} catch (IOException ex) {
			logger.error("Unexpected error!", ex);
		}
	}

	@Override
	public void setBody(ChannelWrapper<APIObject> object) {
		super.setBody(object);
	}
}
