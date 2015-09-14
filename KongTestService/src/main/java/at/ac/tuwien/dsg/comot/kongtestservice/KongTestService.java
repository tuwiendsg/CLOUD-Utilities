/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice;

import at.ac.tuwien.dsg.comot.kongtestservice.model.APIObject;
import at.ac.tuwien.dsg.comot.kongtestservice.utilities.NetworkService;
import java.net.SocketException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
public class KongTestService {
	
	public KongTestService() {
		try {
			APIObject rootApi = new APIObject();
			rootApi.setName("ktsRoot");
			rootApi.setPath("kts");
			//todo: i have to query the ip otherwise it wont work... or maybe a property file
			rootApi.setTargetUrl(String.format("http://%s:8080", NetworkService.getIp()));
			
			HttpEntity requestEntity = new HttpEntity(rootApi);
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> resp = restTemplate.exchange("http://128.130.172.214:8001/apis", HttpMethod.POST, requestEntity, String.class);
		} catch (SocketException ex) {
		}
	}

	@RequestMapping("/")
	public String greeting(){
		return "Welcome to the Kong Test Service!";
	}
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(KongTestService.class, args);
    }
	
}
