/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.discoveryHelper;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DiscoveryException extends Exception {

	/**
	 * Creates a new instance of <code>DiscoveryException</code> without detail
	 * message.
	 */
	public DiscoveryException() {
	}

	/**
	 * Constructs an instance of <code>DiscoveryException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public DiscoveryException(String msg) {
		super(msg);
	}
	
	public DiscoveryException(String msg, Throwable thr) {
		super(msg, thr);
	}
}
