/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry;

import at.ac.tuwien.dsg.comot.messaging.api.Consumer;
import at.ac.tuwien.dsg.comot.messaging.api.Message;
import at.ac.tuwien.dsg.comot.messaging.api.MessageReceivedListener;
import at.ac.tuwien.dsg.comot.messaging.lightweight.ComotMessagingFactory;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@SpringBootApplication
public class RegistryService implements MessageReceivedListener {

	private Consumer consumer;

	public RegistryService() {

		try {
			Properties properties = new Properties();
			InputStream propStream = getClass().getClassLoader().getResourceAsStream("messagingConfig.properties");
			properties.load(propStream);
			propStream.close();

			Config config = new Config();
			config.setSalsaIp(properties.getProperty("salsa.ip", "128.130.172.215"))
					.setSalsaPort(Integer.valueOf(properties.getProperty("salsa.port", "8080")))
					.setServiceName(properties.getProperty("salsa.service", "ManualTestRabbitService"));
					
			this.consumer = ComotMessagingFactory
					.getRabbitMqConsumer()
					.withLightweigthSalsaDiscovery(config)
					.withType("apiRegistry");
			
			this.consumer.addMessageReceivedListener(this);
		} catch (IOException ex) {
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegistryService.class, args);
	}

	@Override
	public void messageReceived(Message message) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
