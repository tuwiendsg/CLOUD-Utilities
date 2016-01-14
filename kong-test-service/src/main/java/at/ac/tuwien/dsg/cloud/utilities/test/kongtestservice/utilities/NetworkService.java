/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.test.kongtestservice.utilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class NetworkService {

	private static Logger logger = LoggerFactory.getLogger(NetworkService.class);

	public static String getIp() throws SocketException {
		for (NetworkInterface nic : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			if (!nic.isLoopback() && !nic.isVirtual() && nic.isUp()) {
				for (InetAddress ad : Collections.list(nic.getInetAddresses())) {
					//test internet connection
					try (SocketChannel socket = SocketChannel.open()) {
						socket.socket().setSoTimeout(5000);

						socket.bind(new InetSocketAddress(ad, 0));
						socket.connect(new InetSocketAddress("google.com", 80));
						//if everything passes the InetAddress should be okay.
						socket.close();
						String ip = ad.getHostAddress();
						logger.trace("Found interface: {}", ip);
						return ip;
					} catch (IOException ex) {
					}
				}
			}
		}

		return null;
	}
}
