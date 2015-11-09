/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryRequest;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryResponse;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
		logger = LoggerFactory.getLogger(DiscoveryRESTService.class);
		this.config = config;
	}

	public boolean checkForDiscovery() {
		try {
			URI statusUri = UriBuilder.fromPath("/isDeployed")
					.host(config.getDiscoveryIp())
					.port(config.getDiscoveryPort())
					.scheme("http")
					.build();

			HttpUriRequest request = new HttpGet(statusUri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(request);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				return true;
			}
		} catch (IllegalStateException ex) {
			if (!ex.getMessage().equals("Target host is null")) {
				throw ex;
			}
		} catch (IOException ex) {
		}

		return false;
	}

	@Override
	public String discoverHost() {
		URI statusUri = UriBuilder.fromPath(restCommand)
				.host(config.getDiscoveryIp())
				.port(config.getDiscoveryPort())
				.scheme("http")
				.build();

		DiscoveryRequest request = new DiscoveryRequest()
				.setServiceName(config.getServiceName());
		RestTemplate template = new RestTemplate();
		
		//todo: find out why I can not send the object
		DiscoveryResponse response = template.exchange(statusUri,
				HttpMethod.POST,
				new HttpEntity(request),
				DiscoveryResponse.class).getBody();

		return super.discoverHost(response.getServiceIp());
	}

}
