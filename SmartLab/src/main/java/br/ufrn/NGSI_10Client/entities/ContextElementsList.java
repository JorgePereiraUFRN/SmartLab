package br.ufrn.NGSI_10Client.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContextElementsList {
	
	private List<ContextElementRequest> contextResponses = new ArrayList<ContextElementRequest>();

	

	public List<ContextElementRequest> getContextResponses() {
		return contextResponses;
	}



	public void setContextResponses(List<ContextElementRequest> contextResponses) {
		this.contextResponses = contextResponses;
	}



	@Override
	public String toString() {
		return "ContextElementsList [contextResponses=" + contextResponses
				+ "]";
	}
	
	

}
