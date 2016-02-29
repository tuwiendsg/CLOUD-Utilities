/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.ServerCluster;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ClusterHelper implements Runnable {
	
	private ServerCluster serverCluster;
	
	public ClusterHelper(ServerCluster serverCluster, 
			ExecutorService executor) {
		this.serverCluster = serverCluster;
		executor.execute(this);
	}

	@Override
	public void run() {
		if(!serverCluster.isDeployed()) {
			serverCluster.deploy();
		}
	}
	
}
