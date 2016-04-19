/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.DecisionSettings;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.ServiceSettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This second configuration class is necessary to avoid a wiring conflict
 * between annotation based and java based beans.
 * 
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Configuration
public class SpringPropertiesConfig {
	@ConfigurationProperties(prefix = "discovery")
	@Bean
	public DiscoverySettings discoverySettings() {
		return new DiscoverySettings();
	}
	
	@ConfigurationProperties(prefix = "kong")
	@Bean(name = "kongSettings")
	public ServiceSettings kongSettings() {
		return new ServiceSettings();
	}
	
	@ConfigurationProperties(prefix = "decision")
	@Bean(name = "decisionSettings")
	public DecisionSettings decisionSettings() {
		return new DecisionSettings();
	}
}
