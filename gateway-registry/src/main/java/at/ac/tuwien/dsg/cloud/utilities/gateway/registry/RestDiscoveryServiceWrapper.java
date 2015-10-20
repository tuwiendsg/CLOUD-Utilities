/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery.DiscoveryRESTService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Service
public class RestDiscoveryServiceWrapper extends DiscoveryRESTService implements Runnable, ApplicationContextAware {
	
	private AnnotationConfigEmbeddedWebApplicationContext ac;
	private boolean shutdown = false;
	
	@Autowired
	public RestDiscoveryServiceWrapper(ConfigService service) {
		super(service.getConfig());
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
			this.ac.register(PostDiscoveryBeans.class);
			this.ac.refresh();
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		this.ac = (AnnotationConfigEmbeddedWebApplicationContext) ac;
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
}
