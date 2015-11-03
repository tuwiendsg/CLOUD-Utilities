/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DiscoveryRESTService;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RestDiscoveryServiceWrapper extends DiscoveryRESTService implements
		Runnable, Shutdownable {
	
	private boolean shutdown = false;
	private RestDiscoveryServiceWrapperCallback cb;
	private ExecutorService executor;
	
	public RestDiscoveryServiceWrapper(Config config, 
			RestDiscoveryServiceWrapperCallback cb) {
		this(config, cb, Executors.newFixedThreadPool(1));
	}
	
	public RestDiscoveryServiceWrapper(Config config, 
			RestDiscoveryServiceWrapperCallback cb, ExecutorService external) {
		super(config);
		this.cb = cb;
		this.executor = external;
		this.executor.execute(this);
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
			this.cb.discoveryIsOnline(this);
		}
	}
	
	@Override
	public void shutdown() {
		this.shutdown = true;
		this.executor.shutdown();
	}
}
