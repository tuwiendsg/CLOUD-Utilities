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
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.DeleteListener;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.RegisterListener;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.SalsaSettings;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.DeleteApiTask;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.RegisterApiTask;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Consumer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.ServerCluster;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Shutdownable;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.JacksonSerializer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.serverCluster.RabbitMQServerCluster;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.serverCluster.ServerConfig;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.DecisionSettings;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.ServiceSettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Configuration
public class SpringConfig {
	
	@Autowired
	private SpringPropertiesConfig propertiesConfig;
	@Autowired
	private KongService kongService;
	@Autowired
	private RegistryService regestryService;
	
	@Autowired
	public Provider<RegisterApiTask> registerApiTaskProvider;
	@Autowired
	public Provider<DeleteApiTask> deleteApiTaskProvider;
	
	
	
	@Bean
	public Discovery serviceDiscovery() {		
		return ComotMessagingFactory.getServiceDiscovery(propertiesConfig
				.discoverySettings(), 
				jacksonSerializer());
	}
	
	@Bean
	public Consumer consumer() {
		//todo: check consumer for shutdown?!
		Consumer bean = ComotMessagingFactory
				.getRabbitMqConsumer(serviceDiscovery());
		bean.addMessageReceivedListener(registerListener());
		bean.addMessageReceivedListener(deleteListener());
		return bean;
	}
	
	@Bean
	public Serializer jacksonSerializer() {
		return new JacksonSerializer();
	}
	
	@Bean
	@Scope("prototype")
	public DeleteApiTask deleteApiTask() {
		return new DeleteApiTask(kongService);
	}
	
	@Bean
	@Scope("prototype")
	public RegisterApiTask registerApiTask() {
		return new RegisterApiTask(kongService, 
				ComotMessagingFactory
						.getRabbitMqProducer(serviceDiscovery()),
				propertiesConfig.decisionSettings());
	}
	
	@Bean
	public RegisterListener registerListener() {
		RegisterListener bean = new RegisterListener(executorService(), 
				registerApiTaskProvider, jacksonSerializer());
		return bean;
	}
	
	@Bean
	public DeleteListener deleteListener() {
		DeleteListener bean = new DeleteListener(executorService(), 
				deleteApiTaskProvider, jacksonSerializer());
		return bean;
	}
	
	@Bean
	public ExecutorService executorService() {
		ExecutorService bean = Executors.newCachedThreadPool();
		regestryService.registerService(new Shutdownable() {
			@Override
			public void shutdown() {
				bean.shutdown();
			}
		});
		return bean;
	}
	
	@ConfigurationProperties(prefix = "salsa")
	@Bean
	public ServerConfig serverConfig() {
		return new SalsaSettings();
	}  
	
	@Bean
	public ServerCluster rabbitMqServerCluster() {
		return new RabbitMQServerCluster(serverConfig());
	}
	
	@Bean
	public ClusterHelper clusterHelper() {
		return new ClusterHelper(rabbitMqServerCluster(), executorService());
	}
}
