/* 
 * Copyright 2015 Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ServiceSettings {
	protected String ip;
	protected int port;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String asUrl() {
		try {
			return (new URL("http", ip, port, "")).toString();
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Malformed config!", ex);
		}
	}
}
