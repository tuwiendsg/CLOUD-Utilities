/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice.utilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Collections;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class NetworkService {

	public static String getIp() throws SocketException {
		for (NetworkInterface nic : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			for (InetAddress ad : Collections.list(nic.getInetAddresses())) {
				//test internet connection
				try (SocketChannel socket = SocketChannel.open()) {
					socket.socket().setSoTimeout(5000);

					socket.bind(new InetSocketAddress(ad, 0));
					socket.connect(new InetSocketAddress("google.com", 80));
					//if everything passes the InetAddress should be okay.
					socket.close();
					return ad.getHostAddress();
				} catch (IOException ex) {
				}
			}
		}
		
		return null;
	}
}
