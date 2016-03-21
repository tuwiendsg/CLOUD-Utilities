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

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.ServerCluster;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Shutdownable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
@EnableZuulProxy
public class RegistryService implements ShutdownService {

	private static Logger logger = LoggerFactory.getLogger(RegistryService.class);

	private List<Shutdownable> shutdownables;

	public RegistryService() {
//		this.listeners = new ArrayList();
		this.shutdownables = new ArrayList<>();
	}

	@PostConstruct
	public void startup() {
		logger.trace("Starting post construct.");
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistryService.class, args);
	}

	@RequestMapping("/")
	public String greeting() {
		return "Welcome to the Gateway Regestry Service!";
	}

	@Override
	public void registerService(Shutdownable service) {
		synchronized(this.shutdownables) {
			this.shutdownables.add(service);
		}
	}

	@PreDestroy
	@Override
	public void shutdownAll() {
		synchronized (this.shutdownables) {
			this.shutdownables.stream().forEach(s -> {
				s.shutdown();
			});
		}
	}
}
