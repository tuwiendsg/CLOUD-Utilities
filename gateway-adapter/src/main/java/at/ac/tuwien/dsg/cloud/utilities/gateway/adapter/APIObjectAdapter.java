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
package at.ac.tuwien.dsg.cloud.utilities.gateway.adapter;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class APIObjectAdapter implements Adapter {
	
	private APIObject object;
	private AdapterService adapterService;
	
	public APIObjectAdapter(AdapterService service) {
		this.adapterService = service;
		this.object = new APIObject();
	}

	@Override
	public Adapter withName(String name) {
		this.object.setName(name);
		return this;
	}

	@Override
	public Adapter withPublicDns(String dns) {
		this.object.setPublicDns(dns);
		return this;
	}

	@Override
	public Adapter withPath(String path) {
		this.object.setPath(path);
		return this;
	}

	@Override
	public Adapter doStripPath(boolean stripPath) {
		this.object.setStripPath(stripPath);
		return this;
	}

	@Override
	public Adapter doPreserveHost(boolean preserveHost) {
		this.object.setPreserveHost(preserveHost);
		return this;
	}

	@Override
	public Adapter withTargetUrl(String targetUrl) {
		this.object.setTargetUrl(targetUrl);
		return this;
	}

	@Override
	public void send() throws NoDiscoveryException {
		this.adapterService.send(this.object);
	}
	
	public void delete() {
		this.adapterService.delete(this.object.getTargetUrl());
	}
}
