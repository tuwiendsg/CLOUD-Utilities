/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice;

import at.ac.tuwien.dsg.comot.gateway.adapter.AdapterService;
import at.ac.tuwien.dsg.comot.gateway.adapter.AdapterServiceImpl;
import at.ac.tuwien.dsg.comot.kongtestservice.utilities.NetworkService;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;
import java.net.SocketException;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger logger = LoggerFactory.getLogger(KongTestService.class);

	private AdapterService adapterService;
	
	public KongTestService() {
		
		Config config = new Config();
		config.setSalsaIp("128.130.172.215")
				.setSalsaPort(8080)
				.setServiceName("ManualTestRabbitService");
		
		this.adapterService = new AdapterServiceImpl(config);
		
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
			logger.error("Error with extracting IP.", ex);
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

	public static void main(String[] args) throws Exception {
		SpringApplication.run(KongTestService.class, args);
	}

}
