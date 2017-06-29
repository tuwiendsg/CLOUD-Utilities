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
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryException;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryRequest;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryResponse;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Serializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RestServiceDiscovery implements Discovery {

	private static Logger logger = LoggerFactory.getLogger(RestServiceDiscovery.class);
	//todo: use other config: this here are ip and port of the discovery
	// and not of SALSA
	private DiscoverySettings discoverySettings;
	private final String restCommand = "/discover";
	private final Serializer serializer;

	public RestServiceDiscovery(DiscoverySettings settings,
			Serializer serializer) {
		logger = LoggerFactory.getLogger(RestServiceDiscovery.class);
		this.discoverySettings = settings;
		this.serializer = serializer;
	}

//	public boolean checkForDiscovery() {
//		try {
//			logger.trace("Checking for discovery at IP {} with port {}!", discoverySettings.getIp(), discoverySettings.getPort());
//			URI statusUri = UriBuilder.fromPath("/isDeployed")
//					.host(discoverySettings.getIp())
//					.port(discoverySettings.getPort())
//					.scheme("http")
//					.build();
//
//			HttpUriRequest request = new HttpGet(statusUri);
//			HttpClient client = HttpClientBuilder.create().build();
//			HttpResponse resp = client.execute(request);
//			logger.trace("Got response: {}", resp.getStatusLine().getStatusCode());
//
//			if (resp.getStatusLine().getStatusCode() == 200) {
//				return true;
//			}
//		} catch (IOException ex) {
//			logger.warn("Exception in discovery check.", ex);
//		}
//
//		return false;
//	}http://examples.javacodegeeks.com/enterprise-java/rest/restful-java-client-with-java-net-url/

	@Override
	public String discoverHost() throws DiscoveryException {
		
		HttpURLConnection httpConnection = null;
		BufferedReader in = null;
		OutputStream out = null;

		try {
			DiscoveryRequest request = new DiscoveryRequest()
					.setServiceName(discoverySettings.getServiceName());
			
			URI statusUri = URI.create(String.format("%s://%s:%d%s", "http", 
					discoverySettings.getIp(),
					discoverySettings.getPort(),
					restCommand));

			httpConnection = (HttpURLConnection) statusUri
					.toURL().openConnection();

			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			out = httpConnection.getOutputStream();
			out.write(serializer.serialze(request));
			
			in = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			
			String input = "";
			String i;
			while((i = in.readLine()) != null) {
				input += i;
			}
			
			DiscoveryResponse response = this.serializer
					.deserilize(input.getBytes(), DiscoveryResponse.class);
			
			return response.getServiceIp();
		} catch (IOException ex) {
			throw new DiscoveryException("Error while requesting host!", ex);
		} finally {
			if(httpConnection != null) {
				httpConnection.disconnect();
			}

			if(in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					logger.warn("Exception during closing stream!", ex);
				}
			}

			if(out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					logger.warn("Exception during closing stream!", ex);
				}
			}
		}
	}

}
