/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.listener;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIResponseObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.RegistryService;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.DeleteApiTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DeleteListener extends AListener<APIResponseObject, DeleteApiTask>{

	@Autowired
	public DeleteListener(RegistryService service) {
		super(service, "deleteApi", DeleteApiTask.class);
	}
	
}
