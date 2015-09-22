/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry.tasks;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.comot.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.comot.gateway.registry.RegistryService;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DeleteApiTask extends ATask<ChannelWrapper<APIResponseObject>> {

	public DeleteApiTask(RegistryService service) {
		super(service);
	}

	@Override
	public void run() {
		this.registryService.deleteApi(this.wrapper.getBody().getId());
	}
	
}
