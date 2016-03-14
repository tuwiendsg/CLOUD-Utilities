/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.test.kongtestservice;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.AdapterService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.AdapterServiceImpl;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.GatewayAdapterFactory;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.NoDiscoveryException;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import at.ac.tuwien.dsg.cloud.utilities.test.kongtestservice.utilities.NetworkService;
import java.net.SocketException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
public class KongTestService {
	
	private static Logger logger = LoggerFactory.getLogger(KongTestService.class);

	private AdapterService adapterService;
	
	@Autowired
	private DiscoverySettings discoverySettings;
	
	public KongTestService() {		
		
	}
	
	@PostConstruct
	private void init() {
		this.adapterService = GatewayAdapterFactory
				.adapterServiceImpl(discoverySettings);
		
		try {
			String ip = NetworkService.getIp();
			
			this.adapterService.createApiAdapter()
					.withName("ktsRoot")
					.withRestPath("kts")
					.withTargetUrl(String.format("http://%s:8080", ip))
					.send();
		} catch (SocketException ex) {
			logger.error("Error with extracting IP.", ex);
		} catch (NoDiscoveryException ex) {
			logger.error("Error while sending API.", ex);
		}
	}
	
	@PreDestroy
	public void shutdown(){
		//todo: blocking mode is needed in here
			//or is it?
		this.adapterService.unregisterAllApis();
	}

	@RequestMapping("/")
	public String greeting() {
		return "Welcome to the Kong Test Service!";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	public String greeting(@RequestBody String name) {
		return String.format("Welcome to the Kong Test Service, %s!", name);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(KongTestService.class, args);
	}

}
