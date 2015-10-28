/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DiscoveryRESTService;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RestDiscoveryServiceWrapper extends DiscoveryRESTService implements
		Runnable {
	
	private boolean shutdown = false;
	private RestDiscoveryServiceWrapperCallback cb;
	
	public RestDiscoveryServiceWrapper(Config config, 
			RestDiscoveryServiceWrapperCallback cb) {
		super(config);
		this.cb = cb;
	}

	@Override
	public void run() {
		String host = "";
		while (!this.checkForDiscovery() && !shutdown) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex1) {
				this.shutdown = true;
			}
		}

		if (!shutdown) {
			this.cb.discoveryIsOnline();
		}
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
}
