/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class KongUserList {
	public List<KongUser> users;

	@JsonProperty("data")
	public List<KongUser> getUsers() {
		return users;
	}

	public void setUsers(List<KongUser> users) {
		this.users = users;
	}
}
