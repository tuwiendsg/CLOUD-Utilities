/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class APIResponseObject {
	private String id;
	private String name;
	private String publicDns;
	private String targetUrl;
	private boolean preserveHost;
	private long createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("public_dns")
	public String getPublicDns() {
		return publicDns;
	}

	public void setPublicDns(String publicDns) {
		this.publicDns = publicDns;
	}

	@JsonProperty("target_url")
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
