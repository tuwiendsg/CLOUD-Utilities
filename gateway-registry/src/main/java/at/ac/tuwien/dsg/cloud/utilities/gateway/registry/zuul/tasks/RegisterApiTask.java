/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul.tasks;

import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.APIObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.adapter.model.ChannelWrapper;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.tasks.ATask;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul.ZuulApiObject;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul.filters.pre.CustomUsageBalancingFilter;
import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class RegisterApiTask extends ATask<ChannelWrapper<APIObject>> {
	
	private ProxyRouteLocator routeLocator;
	private CustomUsageBalancingFilter filter;

	public RegisterApiTask(ProxyRouteLocator routeLocator, 
			CustomUsageBalancingFilter filter) {
		this.routeLocator = routeLocator;
		this.filter = filter;
	}

	@Override
	public void run() {
		
		APIObject api = this.wrapper.getBody();
		
		String restPath = api.getRestPath();
		
		if(routeLocator.getMatchingRoute(restPath) == null) {
			ZuulProperties.ZuulRoute route = 
				new ZuulProperties.ZuulRoute(api.getApiName(), 
						"/"+restPath, 
						restPath, null, true, Boolean.FALSE);
			
			routeLocator.addRoute(route);
		}		
		
		this.filter.addZuulApiObject(ZuulApiObject.fromApiObject(api));
	}
	
}
