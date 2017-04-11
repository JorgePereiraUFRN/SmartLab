package br.ufrn.NGSI_10Client.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContextElementRequest {

	private ContextElement contextElement;

	public ContextElement getContextElement() {
		return contextElement;
	}

	public void setContextElement(ContextElement contextElement) {
		this.contextElement = contextElement;
	}

	@Override
	public String toString() {
		return "ContextElementRequest [contextElement=" + contextElement + "]";
	}
	
	
	
}
