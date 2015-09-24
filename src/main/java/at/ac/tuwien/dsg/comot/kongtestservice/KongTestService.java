/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice;

import at.ac.tuwien.dsg.comot.gateway.adapter.AdapterService;
import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.kongtestservice.utilities.NetworkService;
import java.net.SocketException;
import javax.annotation.PreDestroy;
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
public class KongTestService {

	private AdapterService adapterService;
	
	public KongTestService() {
		try {
			String ip = NetworkService.getIp();
			
			//todo: add a additional part:
				//.newApiRegistration
				//this will probably also fix the getOrCreate issue in send
				//when there is no previous object created (null values)
			this.adapterService.createApiAdapter()
					.withName("ktsRoot")
					.withPath("kts")
					.doStripPath(true)
					.withPublicDns(ip)
					.withTargetUrl(String.format("http://%s:8080", ip))
					.send();
		} catch (SocketException ex) {
		}
	}
	
	@PreDestroy
	public void shutdown(){
		//todo: blocking mode is needed in here
		this.adapterService.unregisterAllApis();
	}

	@RequestMapping("/")
	public String greeting() {
		return "Welcome to the Kong Test Service!";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(KongTestService.class, args);
	}

}
