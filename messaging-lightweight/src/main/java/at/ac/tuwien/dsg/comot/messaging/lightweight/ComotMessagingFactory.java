/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.messaging.lightweight;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ComotMessagingFactory {
	public static RabbitMqConsumerFactory getRabbitMqConsumer() {
		return new RabbitMqConsumerFactory();
	}
}
