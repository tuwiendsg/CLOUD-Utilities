/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.serverCluster.ServerConfig;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class SalsaSettings extends DiscoverySettings implements ServerConfig {

//	@Value("${salsa.ip}")
//	private String ip;
//	@Value("${salsa.port}")
//	private int port;
//	@Value("${salsa.serviceName}")
//	private String serviceName;
//	@Value("${salsa.serverCount}")
	private int serverCount;

//	@Override
//	public String getIp() {
//		return ip;
//	}
//
//	@Override
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	@Override
//	public int getPort() {
//		return port;
//	}
//
//	@Override
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//	@Override
//	public String getServiceName() {
//		return serviceName;
//	}
//
//	@Override
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}

	@Override
	public int getServerCount() {
		return serverCount;
	}

	@Override
	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}
	
}
