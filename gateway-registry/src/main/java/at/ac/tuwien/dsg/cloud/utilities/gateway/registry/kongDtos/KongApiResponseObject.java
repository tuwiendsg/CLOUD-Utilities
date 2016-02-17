/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class KongApiResponseObject extends APIResponseObject {
	private String publicDns;
	private String targetUrl;
	private boolean preserveHost;
	private long createdAt;

	@JsonProperty("request_host")
	public String getPublicDns() {
		return publicDns;
	}

	public void setPublicDns(String publicDns) {
		this.publicDns = publicDns;
	}

	@JsonProperty("upstream_url")
	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	@JsonProperty("preserve_host")
	public boolean isPreserveHost() {
		return preserveHost;
	}

	public void setPreserveHost(boolean preserverHost) {
		this.preserveHost = preserveHost;
	}

	@JsonProperty("created_at")
	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
}
