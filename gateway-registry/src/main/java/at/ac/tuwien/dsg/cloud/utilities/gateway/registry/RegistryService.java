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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
public class RegistryService implements ShutdownService {

	private static Logger logger = LoggerFactory.getLogger(RegistryService.class);

	private List<Shutdownable> shutdownables;
	@Autowired(required = false)
	private ServerCluster serverCluster;
//	private List<AListener> listeners;

//	@Autowired
//	private Consumer consumer;

//	@Autowired
//	private KongURIs kongUris;
//	@Autowired
//	private DiscoverySettings discoverySettings;
//	@Autowired
//	private Provider<DeleteApiTask> deleteApiTaskProvider;
//	@Autowired
//	private Provider<RegisterApiTask> registerApiTaskProvider;
//	@Autowired
//	private DeleteListener deleteListener;
//	@Autowired
//	private RegisterListener registerListener;
//	@Autowired
//	private ExecutorService executorService;

	public RegistryService() {
//		this.listeners = new ArrayList();
		this.shutdownables = new ArrayList<>();
	}

	@PostConstruct
	public void startup() {
		logger.trace("Starting post construct.");
		
		//todo: this should go to the background!!!
		if(serverCluster != null && !serverCluster.isDeployed()) {
			serverCluster.deploy();
		}
//		this.registerListener(registerListener);
//		this.registerListener(deleteListener);

//		RestDiscoveryServiceWrapper discovery
//				= new RestDiscoveryServiceWrapper(discoverySettings, this,
//						this.executorService);
//		this.shutdownables.add(discovery);
	}

//	public void registerListener(AListener listener) {
//		this.consumer
//				.withType(listener.getType())
//				.addMessageReceivedListener(listener);
//	}

//	public void deleteApi(String id) {
//		try {
//			RestTemplate restTemplate = new RestTemplate();
//			restTemplate.delete(this.kongUris.getKongApiIdUri(id));
//		} catch (HttpClientErrorException ex) {
//		}
//	}
//
//	public APIResponseObject registerApi(APIObject apiObject) {
//		try {
//			RequestEntity<APIObject> requestEntity = RequestEntity
//					.put(URI.create(this.kongUris.getKongApisUri()))
//					.contentType(MediaType.APPLICATION_JSON)
//					.accept(MediaType.ALL)
//					.body(apiObject);
//
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<APIResponseObject> resp = restTemplate
//					.exchange(requestEntity, APIResponseObject.class);
//
//			logger.trace("Response from Kong: {}", resp.getBody());
//			return resp.getBody();
//		} catch (HttpStatusCodeException e) {
//			String serverResp = e.getResponseBodyAsString();
//			logger.error(String.format("Exception from server! "
//					+ "Following body was responded %s", serverResp), e);
//
//			APIResponseObject resp = new APIResponseObject();
//			resp.setError(true);
//			resp.setErrorMsg(serverResp);
//			return resp;
//		}
//	}
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistryService.class, args);
	}

//	@Override
//	public void discoveryIsOnline(RestDiscoveryServiceWrapper wrapper) {
//		logger.trace("Discovery done.");
//		//this.discovery = new CachingDiscovery(wrapper);
//		this.shutdownables.remove(wrapper);
//		this.listeners.add(new RegisterListener(this, registerApiTaskProvider));
//		this.listeners.add(new DeleteListener(this, deleteApiTaskProvider));
//	}
//	public Discovery getDiscovery() {
//		return this.discovery;
//	}
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
