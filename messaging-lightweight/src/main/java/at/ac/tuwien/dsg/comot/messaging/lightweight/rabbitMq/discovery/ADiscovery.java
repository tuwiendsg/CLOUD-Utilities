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
package at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.discovery;

import at.ac.tuwien.dsg.comot.messaging.api.Discovery;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class ADiscovery implements Discovery {
	
	protected Discovery backup;
	
	public String discoverHost(String host) {		
		if(host == null && backup != null) {
			return backup.discoverHost();
		}
		
		return host;
	}

	@Override
	public void setBackup(Discovery backup) {
		this.backup = backup;
	}
}
