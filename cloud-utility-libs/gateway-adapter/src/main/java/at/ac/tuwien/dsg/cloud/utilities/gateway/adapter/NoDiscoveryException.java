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
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class NoDiscoveryException extends Exception {

	/**
	 * Creates a new instance of <code>NoDiscoveryException</code> without
	 * detail message.
	 */
	public NoDiscoveryException() {
	}

	/**
	 * Constructs an instance of <code>NoDiscoveryException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public NoDiscoveryException(String msg) {
		super(msg);
	}
}
