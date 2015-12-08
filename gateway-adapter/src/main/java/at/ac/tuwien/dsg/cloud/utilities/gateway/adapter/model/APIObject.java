/* 
 * Copyright 2015 Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model;

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
