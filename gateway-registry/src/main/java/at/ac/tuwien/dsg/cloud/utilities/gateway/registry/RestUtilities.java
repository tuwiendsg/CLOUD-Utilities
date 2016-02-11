/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class RestUtilities {
	private static Logger logger = LoggerFactory.getLogger(RestUtilities.class);
	
	public <T> ResponseEntity<T> simpleRestExchange(RequestEntity reg, Class<T> respType) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.exchange(reg, respType);
		} catch (HttpStatusCodeException e) {
			String serverResp = e.getResponseBodyAsString();
			logger.error(String.format("Exception from server! "
					+ "With status code %s! "
					+ "Following body was responded %s",
					e.getStatusCode().toString(),
					serverResp), e);
		}

		return null;
	}
}
