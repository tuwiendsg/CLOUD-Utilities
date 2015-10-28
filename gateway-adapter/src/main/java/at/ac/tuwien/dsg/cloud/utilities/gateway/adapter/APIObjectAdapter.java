/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
