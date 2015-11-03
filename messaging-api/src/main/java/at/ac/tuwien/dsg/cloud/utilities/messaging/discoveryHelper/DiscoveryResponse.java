/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DiscoveryResponse {
	private String serviceIp;

	public String getServiceIp() {
		return serviceIp;
	}

	public DiscoveryResponse setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
		return this;
	}
}
