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

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapperCallback;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.Shutdownable;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.Task;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUser;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.AListener;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.DeleteListener;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.RegisterListener;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.CachingDiscovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
public class RegistryService implements RestDiscoveryServiceWrapperCallback {

	private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
	private Discovery discovery;
	private ExecutorService executorService;
	private List<Shutdownable> shutdownables;
	private List<AListener> listeners;
	private List<KongUser> allowedUsers;

	@Autowired
	private KongURIs kongUris;
	@Autowired
	private DiscoverySettings discoverySettings;

	public RegistryService() {
		allowedUsers = new ArrayList<>();
	}

	@PostConstruct
	public void startup() {
		logger.trace("Starting post construct.");
		this.shutdownables = new ArrayList<Shutdownable>();
		this.executorService = Executors.newCachedThreadPool();

		RestDiscoveryServiceWrapper discovery
				= new RestDiscoveryServiceWrapper(discoverySettings, this,
						this.executorService);

		this.listeners = new ArrayList<>();
		this.shutdownables.add(discovery);
	}

	@PreDestroy
	public void destroy() {
		this.shutdownables.stream().forEach(s -> {
			s.shutdown();
		});
		this.executorService.shutdown();
	}

	public void execute(Task task) {
		this.executorService.execute(task);
	}

//	private String getUrlForKongApi(String path) {
//		return String.format("http://%s:%d/%s/", kongSettings.getIp(), kongSettings.getPort(), path);
//	}
	public void deleteApi(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete(this.kongUris.getKongApiIdUri(id));
		} catch (HttpClientErrorException ex) {
		}
	}

	public APIResponseObject registerApi(APIObject apiObject) {
		try {
			RequestEntity<APIObject> requestEntity = RequestEntity
					.put(URI.create(this.kongUris.getKongApisUri()))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.ALL)
					.body(apiObject);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<APIResponseObject> resp = restTemplate
					.exchange(requestEntity, APIResponseObject.class);

			logger.trace("Response from Kong: {}", resp.getBody());
			return resp.getBody();
		} catch (HttpStatusCodeException e) {
			String serverResp = e.getResponseBodyAsString();
			logger.error(String.format("Exception from server! "
					+ "Following body was responded %s", serverResp), e);

			APIResponseObject resp = new APIResponseObject();
			resp.setError(true);
			resp.setErrorMsg(serverResp);
			return resp;
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistryService.class, args);
	}

	@Override
	public void discoveryIsOnline(RestDiscoveryServiceWrapper wrapper) {
		logger.trace("Discovery done.");
		this.discovery = new CachingDiscovery(wrapper);
		this.shutdownables.remove(wrapper);
		this.listeners.add(new RegisterListener(this));
		this.listeners.add(new DeleteListener(this));
	}

	public Discovery getDiscovery() {
		return this.discovery;
	}

	@RequestMapping("/")
	public String greeting() {
		return "Welcome to the Gateway Regestry Service!";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/check")
	public boolean check(@RequestBody String user) {
		logger.info("User {} requested authentication check!", user);
		return this.allowedUsers.stream().anyMatch((u) -> u.getUserName().equals(user));
	}

	private String simpleRestExchange(RequestEntity reg, ResponseEntity resp, Class respType) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			resp = restTemplate.exchange(reg, respType);
		} catch (HttpStatusCodeException e) {
			String serverResp = e.getResponseBodyAsString();
			logger.error(String.format("Exception from server! "
					+ "Following body was responded %s", serverResp), e);
			return serverResp;
		}

		return "ok";
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/users/{user}")
	public String register(@PathVariable String user) {

		RequestEntity<String> request = RequestEntity
				.post(URI.create(this.kongUris.getKongConsumersUri()))
				.contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.ALL)
				.body(String.format("username=%s", user));

		ResponseEntity<KongUser> resp = null;
		String ok = this.simpleRestExchange(request, resp, KongUser.class);

		if (!ok.equals("ok")) {
			return String.format("Could not register user due to %s", ok);
		}

		this.allowedUsers.add(resp.getBody());
		return String.format("User %s added successfully!", user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/users/{user}")
	public String remove(@PathVariable String user) {
		
		ResponseEntity<String> resp = null;
		
		this.allowedUsers.removeIf((u) -> {
			if (u.getUserName().equals(user)) {
				 RequestEntity<Void> request = RequestEntity
						.delete(URI.create(this.kongUris.getKongConsumerIdUri(u.getId())))
						.accept(MediaType.ALL)
						.build();
				 
				 simpleRestExchange(request, resp, String.class);
				
				return true;
			}
			return false;
		});

		if (resp == null) {
			return "User not found!";
		}

		return String.format("User %s removed successfully!", user);
	}
}
