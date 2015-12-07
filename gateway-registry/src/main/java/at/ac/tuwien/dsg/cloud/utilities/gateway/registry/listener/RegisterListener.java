/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.RegisterApiTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RegisterListener extends AListener<APIObject, RegisterApiTask> {
	
	@Autowired
	public RegisterListener(RegistryService service) {
		super(service, "registerApiChannel", RegisterApiTask.class);
	}

	@Override
	protected Class<APIObject> getInnerClass() {
		return APIObject.class;
	}
}
