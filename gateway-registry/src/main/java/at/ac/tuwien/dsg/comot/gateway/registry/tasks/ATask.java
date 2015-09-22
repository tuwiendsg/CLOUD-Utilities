/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry.tasks;

import at.ac.tuwien.dsg.comot.gateway.registry.RegistryService;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public abstract class ATask<T> implements Task<T> {

	protected T wrapper;
	protected RegistryService registryService;
	
	protected ATask(RegistryService service) {
		this.registryService = service;
	}
	
	@Override
	public void setBody(T object) {
		this.wrapper = object;
	}
}
