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
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class ADiscovery implements Discovery {
	
	protected static Logger logger = LoggerFactory.getLogger(ADiscovery.class);
	
	protected Discovery backup;
	
	protected String discoverHost(String host) {		
		if(host == null && backup != null) {
			return backup.discoverHost();
		}
		
		if(logger != null) {
			logger.trace("Discovered host: {}", host);
		}
		
		return host;
	}

	@Override
	public void setBackup(Discovery backup) {
		this.backup = backup;
	}
}
