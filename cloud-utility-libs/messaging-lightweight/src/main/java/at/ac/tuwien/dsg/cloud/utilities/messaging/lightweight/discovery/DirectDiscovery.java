/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryException;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DirectDiscovery implements Discovery {

	private DiscoverySettings discoverySettings;
	
	public DirectDiscovery(DiscoverySettings settings) {
		this.discoverySettings = settings;
	}
	
	@Override
	public String discoverHost() throws DiscoveryException {
		return discoverySettings.getIp();
	}
	
}
