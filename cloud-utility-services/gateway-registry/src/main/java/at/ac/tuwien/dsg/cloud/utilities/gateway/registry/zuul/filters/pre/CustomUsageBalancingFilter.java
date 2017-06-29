/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul.filters.pre;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.zuul.ZuulApiObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cloud.client.ServiceInstance;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class CustomUsageBalancingFilter extends ZuulFilter {

	private HashMap<String, List<ZuulApiObject>> services 
			= new HashMap<>();

	@Override
	public String filterType() {
		return "route";
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		return (ctx.getRouteHost() == null
				&& ctx.get("serviceId") != null
				&& ctx.sendZuulResponse());
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String serviceId = (String) ctx.get("serviceId");

		//todo: this is owefull synchronization!! use just for prototype!!
		synchronized (this.services) {
			Optional<ZuulApiObject> res = services.get(serviceId)
					.stream()
					.min(Comparator.comparingInt(ZuulApiObject::getUsage));

			ctx.set("serviceId", null);

			try {
				ctx.setRouteHost(URI.create(res.get().getTargetUrl()).toURL());
			} catch (MalformedURLException ex) {
				//todo: log
			}
		}

		return null;
	}

	public void addZuulApiObject(ZuulApiObject api) {
		synchronized (this.services) {
			if (!this.services.containsKey(api.getRestPath())) {
				this.services.put(api.getRestPath(), new ArrayList<>());
			}

			this.services.get(api.getRestPath()).add(api);
		}
	}

}
