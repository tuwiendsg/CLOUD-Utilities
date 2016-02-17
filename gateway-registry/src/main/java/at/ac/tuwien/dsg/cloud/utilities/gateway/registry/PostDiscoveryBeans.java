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
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.DeleteApiTask;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.RegisterApiTask;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.ComotMessagingFactory;
import javax.inject.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Configuration
public class PostDiscoveryBeans {
	
	@Autowired
	private RegistryService registryService;
	@Autowired
	private KongService kongService;
	
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
						.getRabbitMqProducer(registryService.getDiscovery()));
	}
//	
//	@Bean
//	public DeleteListener deleteListener() {
//		return new DeleteListener(registryService, deleteApiTaskProvider);
//	}
//	
//	@Bean
//	public RegisterListener registerListener() {
//		return new RegisterListener(registryService, registerApiTaskProvider);
//	}
}
