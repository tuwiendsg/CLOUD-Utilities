/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class APIObject {
	public String name;
	public String publicDns;
	public String path;
	public boolean stripPath;
	public boolean preserveHost;
	public String targetUrl;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonProperty("strip_path")
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

	@JsonProperty("target_url")
	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
