/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.registry.listener;

import at.ac.tuwien.dsg.comot.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.comot.gateway.registry.ConfigService;
import at.ac.tuwien.dsg.comot.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.comot.gateway.registry.tasks.RegisterApiTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@Component
public class RegisterListener extends AListener<APIObject, RegisterApiTask> {
	
	@Autowired
	public RegisterListener(RegistryService service, ConfigService config) {
		super(service, config, "registerApiChannel", 
				APIObject.class, RegisterApiTask.class);
	}
}
