/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.comot.gateway.registry.tasks.Task;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class RegistryService {

	private ExecutorService executorService;

	public RegistryService() {
		this.executorService = Executors.newCachedThreadPool();
	}

	public void execute(Task task) {
		this.executorService.execute(task);
	}

	public void deleteApi(String name) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete("http://128.130.172.214:8001/apis/" + name);
		} catch (HttpClientErrorException ex) {
		}
	}

	public void registerApi(APIObject apiObject) {
		RequestEntity<APIObject> requestEntity = RequestEntity
				.put(URI.create("http://128.130.172.214:8001/apis/"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL)
				.body(apiObject);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<APIResponseObject> resp = restTemplate.exchange(requestEntity, APIResponseObject.class);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistryService.class, args);
	}
}