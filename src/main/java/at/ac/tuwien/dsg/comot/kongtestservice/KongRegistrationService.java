/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice;

import at.ac.tuwien.dsg.comot.kongtestservice.model.APIObject;
import at.ac.tuwien.dsg.comot.kongtestservice.model.APIResponseObject;
import at.ac.tuwien.dsg.comot.kongtestservice.utilities.NetworkService;
import java.net.SocketException;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class KongRegistrationService {

	public static void deleteApi(String name) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://128.130.172.214:8001/apis/" + name);
	}

	public static void registerApi(APIObject apiObject) {
		RequestEntity<APIObject> requestEntity = RequestEntity
				.put(URI.create("http://128.130.172.214:8001/apis/"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL)
				.body(apiObject);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<APIResponseObject> resp = restTemplate.exchange(requestEntity, APIResponseObject.class);
	}
}
