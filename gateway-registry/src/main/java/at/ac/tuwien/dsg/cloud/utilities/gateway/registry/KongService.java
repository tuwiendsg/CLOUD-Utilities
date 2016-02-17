/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongApiResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
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
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class KongService {
	private static Logger logger = LoggerFactory.getLogger(KongService.class);
	
	@Autowired
	private KongURIs kongUris;
	
	public KongApiResponseObject registerApi(KongApiObject apiObject) {
		try {
			RequestEntity<KongApiObject> requestEntity = RequestEntity
					.put(URI.create(this.kongUris.getKongApisUri()))
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
	
	public void deleteApi(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.delete(this.kongUris.getKongApiIdUri(id));
		} catch (HttpClientErrorException ex) {
		}
	}
}
