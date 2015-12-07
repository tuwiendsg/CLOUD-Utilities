/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util;

import java.io.IOException;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public interface Serializer<T> {
	byte[] serialze(T object) throws IOException;
	T deserilize(byte[] bytes) throws IOException;
	Serializer<T> withInnerType(Class innerType);
}
