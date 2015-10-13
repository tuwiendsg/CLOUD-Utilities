/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.DiscoveryRequest;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.DiscoveryResponse;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DiscoveryRESTService extends ADiscovery implements Discovery {

	//todo: use other config: this here are ip and port of the discovery
		// and not of SALSA
	private Config config;
	private final String restCommand = "/discover";

	public DiscoveryRESTService(Config config) {
		this.config = config;
	}

	@Override
	public String discoverHost() {
		URI statusUri = UriBuilder.fromPath(restCommand).build();
		
		DiscoveryRequest request = new DiscoveryRequest()
				.setServiceName(config.getServiceName());
		RestTemplate template = new RestTemplate();
		DiscoveryResponse response = template.exchange(statusUri, 
				HttpMethod.POST, 
				new HttpEntity<>(request), 
				DiscoveryResponse.class).getBody();

		return super.discoverHost(response.getServiceIp());
	}

}
