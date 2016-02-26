/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings;

import at.ac.tuwien.dsg.comot.messaging.rabbitMq.ServerConfig;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class SalsaSettings implements ServerConfig {

	@Value("${salsa.ip}")
	private String ip;
	@Value("${salsa.port}")
	private int port;
	@Value("${salsa.serviceName}")
	private String serviceName;
	@Value("${salsa.serverCount}")
	private int serverCount;

	@Override
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public int getServerCount() {
		return serverCount;
	}

	@Override
	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}
	
}