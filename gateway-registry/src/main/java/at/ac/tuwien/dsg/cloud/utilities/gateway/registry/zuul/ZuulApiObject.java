/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ZuulApiObject extends APIObject {
	
	private int usage;
	
	public ZuulApiObject() {
		usage = 0;
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}
	
	public static ZuulApiObject fromApiObject(APIObject peer) {
		ZuulApiObject apiObject = new ZuulApiObject();
		apiObject.setApiName(peer.getApiName());
		apiObject.setRestPath(peer.getRestPath());
		apiObject.setTargetUrl(peer.getTargetUrl());
		return apiObject;
	}
}
