/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
