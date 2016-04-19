/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.ServiceSettings;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Configuration
public class TestContext {

	@Autowired
	Environment env;

	@Bean(name = "kongSettings")
	public ServiceSettings kongSettings() {
		ServiceSettings settings = new ServiceSettings();
		settings.setIp("127.0.0.1");
		settings.setPort(8001);
		return settings;
	}

	@Bean
	public KongURIs kongURIs() {
		return new KongURIs(kongSettings());
	}

	@Bean
	public RestUtilities restUtilities() {
		return new RestUtilities();
	}
	
	@Bean
	public KongService kongService() {
		return new KongService(kongURIs(), restUtilities());
	}

	@Bean
	@Lazy
	public UserController userController() {
		return new UserController(kongURIs(), restUtilities(), kongService());
	}
}
