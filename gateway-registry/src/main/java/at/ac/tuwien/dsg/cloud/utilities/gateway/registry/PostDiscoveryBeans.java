/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.DeleteListener;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener.RegisterListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class PostDiscoveryBeans {
	
	@Autowired
	private RegistryService registryService;
	@Autowired
	private ConfigService configService;
	
	@Bean
	public DeleteListener deleteListener() {
		return new DeleteListener(registryService, configService);
	}
	
	@Bean
	public RegisterListener registerListener() {
		return new RegisterListener(registryService, configService);
	}
}
