/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry;

import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Service
public class ConfigService {

	public Config getConfig() {

		try {
			Properties properties = new Properties();
			InputStream propStream = getClass().getClassLoader()
					.getResourceAsStream("messagingConfig.properties");
			properties.load(propStream);
			propStream.close();

			Config config = new Config();
			config.setSalsaIp(properties
					.getProperty("salsa.ip", "128.130.172.215"))
					.setSalsaPort(Integer.valueOf(properties
									.getProperty("salsa.port", "8080")))
					.setServiceName(properties
							.getProperty("salsa.service", "ManualTestRabbitService"));
			return config;
		} catch (IOException ex) {
			Logger.getLogger(ConfigService.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
}
