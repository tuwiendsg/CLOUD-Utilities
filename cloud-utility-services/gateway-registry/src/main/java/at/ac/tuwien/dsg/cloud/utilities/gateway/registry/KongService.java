/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongPlugin;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongPluginResponse;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUserKeyObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUserList;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class KongService {

	private static Logger logger = LoggerFactory.getLogger(KongService.class);

	private KongURIs kongUris;
	private RestUtilities restUtilities;
	
	@Autowired
	public KongService(KongURIs kongUris, RestUtilities restUtilities) {
		this.kongUris = kongUris;
		this.restUtilities = restUtilities;
	}

	public KongApiResponseObject registerApi(KongApiObject apiObject) {
		try {
			RequestEntity<KongApiObject> requestEntity = RequestEntity
					.post(URI.create(this.kongUris.getKongApisUri()))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.ALL)
					.body(apiObject);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<KongApiResponseObject> resp = restTemplate
					.exchange(requestEntity, KongApiResponseObject.class);

			logger.trace("Response from Kong: {}", resp.getBody());
			return resp.getBody();
		} catch (HttpStatusCodeException e) {
			String serverResp = e.getResponseBodyAsString();
			logger.error(String.format("Exception from server! "
					+ "Following body was responded %s", serverResp), e);

			KongApiResponseObject resp = new KongApiResponseObject();
			resp.setError(true);
			resp.setErrorMsg(serverResp);
			return resp;
		}
	}

	public KongPluginResponse enablePlugin(KongApiObject apiObject,
			KongPlugin plugin) {
		try {
			RequestEntity<KongPlugin> requestEntity = RequestEntity
					.post(URI.create(this.kongUris
							.getKongPluginsForApiUri(apiObject.getApiName())))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.ALL)
					.body(plugin);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<KongPluginResponse> resp = restTemplate
					.exchange(requestEntity, KongPluginResponse.class);

			logger.trace("Response from Kong: {}", resp.getBody());
			return resp.getBody();
		} catch (HttpStatusCodeException e) {
			String serverResp = e.getResponseBodyAsString();
			logger.error(String.format("Exception from server! "
					+ "Following body was responded %s", serverResp), e);
		}
		return null;
	}

	public void deleteApi(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete(this.kongUris.getKongApiIdUri(id));
		} catch (HttpClientErrorException ex) {
		}
	}

	public KongUserKeyObject createKeyForUser(String userName) {
		RequestEntity<Void> requestEntity = RequestEntity
				.post(URI.create(this.kongUris
						.getKongKeyForConsumerUri(userName)))
				//.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL)
				.build();
		
		return this.restUtilities.simpleRestExchange(requestEntity, 
				KongUserKeyObject.class).getBody();
	}
}
