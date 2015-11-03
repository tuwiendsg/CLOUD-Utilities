/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.api;

import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryRequest;
import at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper.DiscoveryResponse;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public interface DiscoveryService {
	DiscoveryResponse discover(DiscoveryRequest request);
	boolean isDeployed();
}
