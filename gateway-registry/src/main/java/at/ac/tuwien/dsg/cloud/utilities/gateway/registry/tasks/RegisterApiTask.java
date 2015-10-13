/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.ConfigService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DiscoveryRESTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RegisterApiTask extends ATask<ChannelWrapper<APIObject>> {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterApiTask.class);
	
	private ConfigService confService;
	private Producer producer;
	
	public RegisterApiTask(RegistryService service, ConfigService confService) {
		super(service);
		this.confService = confService;
	}

	@Override
	public void run() {
		try {
			APIResponseObject resp = this.registryService.registerApi(this
					.wrapper.getBody());
			
			ObjectMapper mapper = new ObjectMapper();
			
			Message  response = ComotMessagingFactory
					.getRabbitMqMessage()
					.withType(this.wrapper.getResponseChannelName())
					.setMessage(mapper.writeValueAsBytes(resp));
			
			this.producer.sendMessage(response);
		} catch (IOException ex) {
			logger.error("Unexpected error!", ex);
		}
	}

	@Override
	public void setBody(ChannelWrapper<APIObject> object) {
		super.setBody(object);
		
		this.producer = ComotMessagingFactory
				.getRabbitMqProducer(
						new DiscoveryRESTService(
								this.confService.getConfig()
						)
				);
	}
}
