/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight;

import at.ac.tuwien.dsg.cloud.utilities.messaging.api.CachingProducer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Message;
import at.ac.tuwien.dsg.cloud.utilities.messaging.api.Producer;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.rabbitMq.channel.ChannelException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class MapCachingProducer implements CachingProducer, Runnable {

	private Map<String, Message> cache;
	private ExecutorService executorService;
	private Producer producer;

	public MapCachingProducer(Producer producer) {
		this(producer, new HashMap<>());
	}

	public MapCachingProducer(Producer producer, Map<String, Message> map) {
		this.producer = producer;
		this.cache = map;
		this.executorService = Executors.newFixedThreadPool(1);
	}

	@Override
	public void sendMessage(Message message) {
		try {
			producer.sendMessage(message);
		} catch (ChannelException ex) {
			synchronized (this.cache) {
				this.cache.put(message.getMessageKey(), message);

				if (this.cache.size() == 1) {
					this.executorService.execute(this);
				}
			}
		}
	}

	@Override
	public boolean removeMessageFromCache(String key) {
		synchronized(this.cache) {			
			if(!this.cache.containsKey(key)) {
				return false;
			}
			
			this.cache.remove(key);
			return true;
		}
	}

	@Override
	public void run() {
		boolean done = false;

		while (!done) {
			try {
				synchronized (this.cache) {
					
					Iterator<Message> i = this.cache.values().iterator();
					while(i.hasNext()) {
						this.producer.sendMessage(i.next());
					}
					this.cache.clear();
					done = true;
				}
			} catch (ChannelException ex) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex1) {
					//ignore
				}
			}
		}
	}

	@Override
	public void clearCache() {
		synchronized(this.cache) {
			this.cache.clear();
		}
	}
}
