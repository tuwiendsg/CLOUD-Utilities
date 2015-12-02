/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapperCallback;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.Shutdownable;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.Task;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
public class RegistryService implements RestDiscoveryServiceWrapperCallback {
	private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
	private Discovery discovery;
	private ExecutorService executorService;
	private List<Shutdownable> shutdownables;
	private List<AListener> listeners;
	
	@Autowired
	private KongSettings kongSettings;
	@Autowired
	private DiscoverySettings discoverySettings;

	public RegistryService() {
	}

	@PostConstruct
	public void startup() {
		logger.trace("Starting post construct.");
		this.shutdownables = new ArrayList<Shutdownable>();
		this.executorService = Executors.newCachedThreadPool();
		
		RestDiscoveryServiceWrapper discovery = 
				new RestDiscoveryServiceWrapper(discoverySettings, this, 
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
	
	private String getUrlForKongApi() {
		return String.format("http://%s:%d/%s/",kongSettings.getIp(), kongSettings.getPort(), "apis");
	}

	public void deleteApi(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete(this.getUrlForKongApi() + id);
		} catch (HttpClientErrorException ex) {
		}
	}

	public APIResponseObject registerApi(APIObject apiObject) {
		RequestEntity<APIObject> requestEntity = RequestEntity
				.put(URI.create(this.getUrlForKongApi()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL)
				.body(apiObject);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<APIResponseObject> resp = restTemplate
				.exchange(requestEntity, APIResponseObject.class);

		logger.trace("Response from Kong: {}", resp.getBody());
		return resp.getBody();
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
}
