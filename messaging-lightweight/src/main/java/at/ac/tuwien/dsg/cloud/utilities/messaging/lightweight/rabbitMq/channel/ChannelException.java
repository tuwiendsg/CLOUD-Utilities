/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ChannelException extends RuntimeException {

	/**
	 * Creates a new instance of <code>RabbitChannelException</code> without
	 * detail message.
	 */
	public ChannelException() {
	}

	/**
	 * Constructs an instance of <code>RabbitChannelException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public ChannelException(String msg) {
		super(msg);
	}
	
	public ChannelException(String msg, Throwable thr) {
		super(msg, thr);
	}
}
