/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.ServiceSettings;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class KongURIs {

	public static final String KONG_APIS_URI = "/apis";
	public static final String KONG_CONSUMERS_URI = "/consumers";
	public static final String KONG_PLUGINS_URI = "/plugins";
	public static final String KONG_CONSUMER_KEY_AUTH = "/key-auth";

	private ServiceSettings kongSettings;

	@Autowired
	public KongURIs(@Qualifier("kongSettings") ServiceSettings settings) {
		this.kongSettings = settings;
	}

	public String getKongAdminUri() {
		return String.format("http://%s:%d", kongSettings.getIp(), kongSettings.getPort());
	}

	private String getBasicCombo(String part1, String part2) {
		if (part2.startsWith("/")) {
			return String.format("%s%s", part1, part2);
		}
		
		return String.format("%s/%s", part1, part2);
	}

	public String getKongApisUri() {
		return this.getBasicCombo(this.getKongAdminUri(), KONG_APIS_URI);
	}

	public String getKongApiIdUri(String id) {
		return this.getBasicCombo(this.getKongApisUri(), id);
	}

	public String getKongConsumersUri() {
		return this.getBasicCombo(this.getKongAdminUri(), KONG_CONSUMERS_URI);
	}

	public String getKongConsumerIdUri(String id) {
		return this.getBasicCombo(this.getKongConsumersUri(), id);
	}
	
	public String getKongPluginsForApiUri(String api) {
		return this.getBasicCombo(this.getKongApisUri(), 
				this.getBasicCombo(api, KONG_PLUGINS_URI));
	}
	
	public String getKongKeyForConsumerUri(String user) {
		return this.getBasicCombo(this.getBasicCombo(this.getKongConsumersUri(), 
				user), KONG_CONSUMER_KEY_AUTH);
	}
}
