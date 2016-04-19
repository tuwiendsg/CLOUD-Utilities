/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.settings;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DecisionSettings extends ServiceSettings {
	private String restPath;

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}
	
	@Override
	public String asUrl() {
		try {
			return (new URL("http", super.ip, super.port, restPath)).toString();
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Malformed config!", ex);
		}
	}
}
