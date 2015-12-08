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
public class JacksonSerializer<T> implements Serializer<T> {
	
	private ObjectMapper mapper;
	private Class<T> clazz;
	private JavaType javaType;
	
	public JacksonSerializer(Class<T> clazz) {
		this.mapper = new ObjectMapper();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialze(T object) throws IOException {
		return mapper.writeValueAsBytes(object);
	}

	@Override
	public T deserilize(byte[] bytes) throws IOException {
		if(this.javaType != null) {
			T res = mapper.readValue(bytes, javaType);
			this.javaType = null;
			return res;
		}
		return mapper.readValue(bytes, clazz);
	}

	/**
	 * Some serializers need an inner type when de/serializing generalized objects.
	 * 
	 * This method provides you with the ability to set such an inner class once!
	 * This means you have to set it before every call to de/serialize
	 * when using it with generalized objects.
	 * 
	 * 
	 * @param innerType
	 * @return 
	 */
	@Override
	public Serializer<T> withInnerType(Class innerType) {
		this.javaType = mapper.getTypeFactory().constructParametrizedType(clazz, clazz, innerType);
		return this;
	}
	
}
