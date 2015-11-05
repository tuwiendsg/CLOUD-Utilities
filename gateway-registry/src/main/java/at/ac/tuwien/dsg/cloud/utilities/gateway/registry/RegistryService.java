/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.ConfigService;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

	private Discovery discovery;
	private ExecutorService executorService;
	private List<Shutdownable> shutdownables;
	private List<AListener> listeners;

	public RegistryService() {
	}

	@PostConstruct
	public void startup() {
		this.shutdownables = new ArrayList<Shutdownable>();
		this.executorService = Executors.newCachedThreadPool();
		ConfigService service = new ConfigService();
		RestDiscoveryServiceWrapper discovery = 
				new RestDiscoveryServiceWrapper(service.getConfig(), this, 
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

	public void deleteApi(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete("http://128.130.172.214:8001/apis/" + id);
		} catch (HttpClientErrorException ex) {
		}
	}

	public APIResponseObject registerApi(APIObject apiObject) {
		RequestEntity<APIObject> requestEntity = RequestEntity
				.put(URI.create("http://128.130.172.214:8001/apis/"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL)
				.body(apiObject);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<APIResponseObject> resp = restTemplate
				.exchange(requestEntity, APIResponseObject.class);

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
		this.discovery = new CachingDiscovery(wrapper);
		this.shutdownables.remove(wrapper);
		this.listeners.add(new RegisterListener(this));
		this.listeners.add(new DeleteListener(this));
	}
	
	public Discovery getDiscovery() {
		return this.discovery;
	}
}
