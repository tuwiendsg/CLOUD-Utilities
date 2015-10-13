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
public interface AdapterService {
	Adapter createApiAdapter();
	void unregisterAllApis();
	void send(APIObject object);
	void delete(String targetUrl);
}
