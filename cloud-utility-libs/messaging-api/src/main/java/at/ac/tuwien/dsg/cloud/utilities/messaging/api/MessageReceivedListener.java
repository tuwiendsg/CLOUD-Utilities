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
package at.ac.tuwien.dsg.cloud.utilities.messaging.api;

import java.util.EventListener;

/**
 * 
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public interface MessageReceivedListener extends EventListener {
	
	public static String defaultType = "default";
	
	/**
	 * Gets fired when a message has been received by the consumer.
	 * @param message - The received message.
	 */
	void messageReceived(Message message);
	
	/**
	 * This specifies the type of messages this listener is interested in.
	 * 
	 * If not defined the listener will use a default type. Please note that
	 * it is not guaranteed that the default type will be notified only
	 * once per message.
	 * 
	 * @return 
	 */
	default String getType() {
		return defaultType;
	}
}
