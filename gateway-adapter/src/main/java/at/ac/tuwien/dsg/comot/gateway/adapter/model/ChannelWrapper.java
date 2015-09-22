/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.gateway.adapter.model;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class ChannelWrapper<T> {
	private T body;
	private String responseChannelName;

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getResponseChannelName() {
		return responseChannelName;
	}

	public void setResponseChannelName(String responseChannelName) {
		this.responseChannelName = responseChannelName;
	}
}
