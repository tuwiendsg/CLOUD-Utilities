/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class CachingDiscovery extends ADiscovery implements Discovery {

	private String host;
	
	public CachingDiscovery(Discovery discovery) {
		this.setBackup(discovery);
	}
	
	@Override
	public String discoverHost() {
		host = this.discoverHost(host);
		return host;
	}	
}
