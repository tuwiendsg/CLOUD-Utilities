/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ConfigService {
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigService.class);

	public Config getConfig() {

		try {
			//todo: should not crash when no config file available
			Properties properties = new Properties();
			InputStream propStream = getClass().getClassLoader()
					.getResourceAsStream("messagingConfig.properties");
			properties.load(propStream);
			propStream.close();

			Config config = new Config();
			config.setDiscoveryIp(properties
					.getProperty("discovery.ip", "128.130.172.214"))
					.setDiscoveryPort(Integer.valueOf(properties
									.getProperty("discovery.port", "8580")))
					.setServiceName(properties
							.getProperty("discovery.service", "ManualTestRabbitService"));
			return config;
		} catch (IOException ex) {
			logger.error("Could not retrieve config!", ex);
		}

		return null;
	}
}
