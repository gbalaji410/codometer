package com.walmartlabs.codometer.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "computes")
public class Computes {
    
	@XmlElement
	private List<String> computes;

	public List<String> getComputes() {
		return computes;
	}

	public void setComputes(List<String> computes) {
		this.computes = computes;
	}
}