/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ServerConfig extends Config<ServerConfig> {
	private int serverCount;

	public int getServerCount() {
		return serverCount;
	}

	public Config setServerCount(int rabbitServerCount) {
		this.serverCount = rabbitServerCount;
		return this;
	}
}
