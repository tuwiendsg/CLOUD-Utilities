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
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * 
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RabbitMqMessage extends TypeHandler<RabbitMqMessage> 
		implements Message, Serializable {
	
	private byte[] content;
	private String key;
	
	public RabbitMqMessage() {
		this.key = UUID.randomUUID().toString();
	}

	@Override
	public RabbitMqMessage setMessage(byte[] content) {
		this.content = content;
		return this;
	}

	@Override
	public byte[] getMessage() {
		return content;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o == null) {
			return false;
		}
		
		if(!(o instanceof RabbitMqMessage)){
			return false;
		}
		
		RabbitMqMessage msg = (RabbitMqMessage)o;
		
		return new EqualsBuilder()
				.appendSuper(super.equals(msg))
				.append(content, msg.content)
				.isEquals();
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 37 * hash + Arrays.hashCode(this.content);
		return hash;
	}

	@Override
	public String getMessageKey() {
		return this.key;
	}

	@Override
	public Message setMessageKey(String key) {
		this.key = key;
		return this;
	}
}
