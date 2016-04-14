/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.serverCluster.ServerConfig;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class SalsaSettings extends DiscoverySettings implements ServerConfig {

	private int serverCount;
	private boolean deploy;

	@Override
	public int getServerCount() {
		return serverCount;
	}

	@Override
	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}

	public void setDeploy(boolean deploy) {
		this.deploy = deploy;
	}

	@Override
	public boolean getDeploy() {
		return deploy;
	}
}
