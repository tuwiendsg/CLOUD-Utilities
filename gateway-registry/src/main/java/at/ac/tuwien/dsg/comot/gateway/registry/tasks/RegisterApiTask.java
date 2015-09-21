/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry.tasks;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.gateway.registry.RegistryService;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RegisterApiTask implements Task<APIObject> {
	
	private APIObject api;
	private RegistryService registryService;
	
	public RegisterApiTask(RegistryService service) {
		this.registryService = service;
	}

	@Override
	public void run() {
		this.registryService.registerApi(api);
	}

	@Override
	public void setBody(APIObject object) {
		this.api = object;
	}
}
