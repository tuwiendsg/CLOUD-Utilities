/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class KongApiObject extends APIObject {
	
	private String publicDns;
	private boolean stripPath;
	private boolean preserveHost;
	
	@Override
	@JsonProperty("name")
	public String getApiName() {
		return super.apiName;
	}
	
	@Override
	@JsonProperty("upstream_url")
	public String getTargetUrl() {
		return super.targetUrl;
	}
	
	@Override
	@JsonProperty("request_path")
	public String getRestPath() {
		return super.restPath;
	}
	
	@JsonProperty("request_host")
	public String getPublicDns() {
		return publicDns;
	}

	public void setPublicDns(String publicDns) {
		this.publicDns = publicDns;
	}

	@JsonProperty("strip_request_path")
	public boolean isStripPath() {
		return stripPath;
	}

	public void setStripPath(boolean stripPath) {
		this.stripPath = stripPath;
	}

	@JsonProperty("preserve_host")
	public boolean isPreserveHost() {
		return preserveHost;
	}

	public void setPreserveHost(boolean preserveHost) {
		this.preserveHost = preserveHost;
	}
	
	public static KongApiObject fromApiObject(APIObject peer) {
		KongApiObject kObject = new KongApiObject();
		kObject.setApiName(peer.getApiName());
		kObject.setRestPath(peer.getRestPath());
		kObject.setTargetUrl(peer.getTargetUrl());
		kObject.setStripPath(true);
		return kObject;
	}
}
