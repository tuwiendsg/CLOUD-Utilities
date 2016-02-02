/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class KongUser {
	private String userName;
	private long createdAt;
	private String id;

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof KongUser)) {
			return false;
		}
		
		KongUser user = (KongUser)obj;
		
		if(!this.id.equals(user.id)) {
			return false;
		}
		
		if(this.createdAt != user.createdAt) {
			return false;
		}
		
		if(!this.userName.equals(user.userName)) {
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + Objects.hashCode(this.userName);
		hash = 29 * hash + (int) (this.createdAt ^ (this.createdAt >>> 32));
		hash = 29 * hash + Objects.hashCode(this.id);
		return hash;
	}
	

	@JsonProperty("username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("created_at")
	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
