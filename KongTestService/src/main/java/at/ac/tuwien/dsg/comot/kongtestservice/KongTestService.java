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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
@RestController
public class KongTestService {

	public KongTestService() {
		try {
			KongRegistrationService.deleteApi("ktsRoot");
			
			String ip = NetworkService.getIp();
			
			APIObject rootApi = new APIObject();
			rootApi.setName("ktsRoot");
			rootApi.setPath("kts");
			rootApi.setPublicDns(ip);
			//todo: i have to query the ip otherwise it wont work... or maybe a property file
			rootApi.setTargetUrl(String.format("http://%s:8080", ip));
			
			KongRegistrationService.registerApi(rootApi);
		} catch (SocketException ex) {
		}
	}

	@RequestMapping("/")
	public String greeting() {
		return "Welcome to the Kong Test Service!";
	}

	/*@RequestMapping("/test")
	 public APIObject test(){
	 try {
	 APIObject rootApi = new APIObject();
	 rootApi.setName("ktsRoot");
	 rootApi.setPath("kts");
	 //todo: i have to query the ip otherwise it wont work... or maybe a property file
	 rootApi.setTargetUrl(String.format("http://%s:8080", NetworkService.getIp()));
			
	 return rootApi;
	 } catch (SocketException ex) {
	 Logger.getLogger(KongTestService.class.getName()).log(Level.SEVERE, null, ex);
	 }
		
	 return null;
	 }*/
	public static void main(String[] args) throws Exception {
		SpringApplication.run(KongTestService.class, args);
	}

}
