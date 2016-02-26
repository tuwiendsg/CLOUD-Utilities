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
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class JacksonSerializer implements Serializer {
	
	private final ObjectMapper mapper;
	
	public JacksonSerializer() {
		mapper = new ObjectMapper();
	}

	@Override
	public <T> byte[] serialze(T object) throws IOException {
		return mapper.writeValueAsBytes(object);
	}

	@Override
	public <T> T deserilize(byte[] bytes, Class<T> clazz) throws IOException {
		return this.deserilize(bytes, clazz, null);
	}
	
	@Override
	public <T> T deserilize(byte[] bytes, Class<T> clazz, Class innerType) 
			throws IOException {
		if(innerType != null) {
			JavaType javaType = mapper.getTypeFactory()
					.constructParametrizedType(clazz, clazz, innerType);
			T res = mapper.readValue(bytes, javaType);
			return res;
		}
		return mapper.readValue(bytes, clazz);
	}
}
