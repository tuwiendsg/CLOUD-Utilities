/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.KongSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class KongURIs {
	public String KONG_APIS_URI = "apis";
	public String KONG_CONSUMERS_URI = "consumers";
	
	@Autowired
	private KongSettings kongSettings;
	
	public String getKongAdminUri() {
		return String.format("http://%s:%d", kongSettings.getIp(), kongSettings.getPort());
	}
	
	private String getBasicCombo(String part1, String part2) {
		return String.format("%s/%s", part1, part2);
	}
	
	public String getKongApisUri() {
		return this.getBasicCombo(this.getKongAdminUri(), this.KONG_APIS_URI);
	}
	
	public String getKongApiIdUri(String id) {
		return this.getBasicCombo(this.getKongApisUri(), id);
	}
	
	public String getKongConsumersUri() {
		return this.getBasicCombo(this.getKongAdminUri(), this.KONG_CONSUMERS_URI);
	}
	
	public String getKongConsumerIdUri(String id) {
		return this.getBasicCombo(this.getKongConsumersUri(), id);
	}
}
