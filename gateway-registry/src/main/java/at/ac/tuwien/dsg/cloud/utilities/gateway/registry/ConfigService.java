/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Service
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
			config.setSalsaIp(properties
					.getProperty("salsa.ip", "128.130.172.214"))
					.setSalsaPort(Integer.valueOf(properties
									.getProperty("salsa.port", "8580")))
					.setServiceName(properties
							.getProperty("salsa.service", "ManualTestRabbitService"));
			return config;
		} catch (IOException ex) {
			logger.error("Could not retrieve config!", ex);
		}

		return null;
	}
}