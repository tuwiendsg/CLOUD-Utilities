/* 
 * Copyright 2015 Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryRequest;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryResponse;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
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
	private DiscoverySettings discoverySettings;
	private final String restCommand = "/discover";

	public DiscoveryRESTService(DiscoverySettings settings) {
		logger = LoggerFactory.getLogger(DiscoveryRESTService.class);
		this.discoverySettings = settings;
	}

	public boolean checkForDiscovery() {
		try {
			logger.trace("Checking for discovery at IP {} with port {}!", discoverySettings.getIp(), discoverySettings.getPort());
			URI statusUri = UriBuilder.fromPath("/isDeployed")
					.host(discoverySettings.getIp())
					.port(discoverySettings.getPort())
					.scheme("http")
					.build();

			HttpUriRequest request = new HttpGet(statusUri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(request);
			logger.trace("Got response: {}", resp.getStatusLine().getStatusCode());
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
				.host(discoverySettings.getIp())
				.port(discoverySettings.getPort())
				.scheme("http")
				.build();

		DiscoveryRequest request = new DiscoveryRequest()
				.setServiceName(discoverySettings.getServiceName());
		RestTemplate template = new RestTemplate();
		
		//todo: find out why I can not send the object
		DiscoveryResponse response = template.exchange(statusUri,
				HttpMethod.POST,
				new HttpEntity(request),
				DiscoveryResponse.class).getBody();

		return super.discoverHost(response.getServiceIp());
	}

}
