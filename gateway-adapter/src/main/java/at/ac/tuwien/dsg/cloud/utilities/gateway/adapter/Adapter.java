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
public interface Adapter {	
	Adapter withName(String name);
	Adapter withPublicDns(String dns);
	Adapter withPath(String path);
	Adapter doStripPath(boolean stripPath);
	Adapter doPreserveHost(boolean preserveHost);
	Adapter withTargetUrl(String targetUrl);
	void send() throws NoDiscoveryException;
}
