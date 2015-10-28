/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.RestDiscoveryServiceWrapperCallback;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.Task;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class RegistryService implements RestDiscoveryServiceWrapperCallback,
		ApplicationContextAware {

	private RestDiscoveryServiceWrapper discovery;
	private ExecutorService executorService;
	private AnnotationConfigEmbeddedWebApplicationContext ac;
	@Autowired
	private ConfigService service;

	public RegistryService() {
	}

	@PostConstruct
	public void startup() {
		this.executorService = Executors.newCachedThreadPool();
		this.discovery = new RestDiscoveryServiceWrapper(service.getConfig(), this);
		this.executorService.execute(discovery);
	}

	@PreDestroy
	public void destroy() {
		this.discovery.shutdown();
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
	public void discoveryIsOnline() {
		this.ac.register(PostDiscoveryBeans.class);
		this.ac.refresh();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		this.ac = (AnnotationConfigEmbeddedWebApplicationContext) ac;
	}
}
