/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings.KongSettings;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
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

	@Bean
	public KongSettings kongSettings() {
		return new KongSettings();
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
	@Lazy
	public UserController userController() {
		return new UserController(kongURIs(), restUtilities());
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();

		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("application.yml"));
		
		conf.setProperties(yaml.getObject());
		return conf;
	}
}
