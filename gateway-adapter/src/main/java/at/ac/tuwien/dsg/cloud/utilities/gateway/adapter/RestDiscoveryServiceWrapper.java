/* 
 * Copyright 2015 Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DiscoveryRESTService;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RestDiscoveryServiceWrapper extends DiscoveryRESTService 
		implements Runnable, Shutdownable {
	
	private boolean shutdown = false;
	private RestDiscoveryServiceWrapperCallback cb;
	private ExecutorService executor;
	
	public RestDiscoveryServiceWrapper(DiscoverySettings settings, 
			RestDiscoveryServiceWrapperCallback cb) {
		this(settings, cb, Executors.newFixedThreadPool(1));
	}
	
	public RestDiscoveryServiceWrapper(DiscoverySettings settings, 
			RestDiscoveryServiceWrapperCallback cb, ExecutorService external) {
		super(settings);
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
