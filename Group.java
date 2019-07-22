package com.purchaseordersequence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {
	
	private List<Order> orders;
	
	private Set<Integer> uniqueOrigins;
	
	private List<Integer> endpoints;
	
	private boolean mergeDone;

	public Group(){
		orders = new ArrayList<>();
		uniqueOrigins = new HashSet<>();
		endpoints = new ArrayList<>();
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Set<Integer> getUniqueOrigins() {
		return uniqueOrigins;
	}

	public void setUniqueOrigins(Set<Integer> uniqueOrigins) {
		this.uniqueOrigins = uniqueOrigins;
	}

	public List<Integer> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Integer> endpoints) {
		this.endpoints = endpoints;
	}

	public boolean isMergeDone() {
		return mergeDone;
	}

	public void setMergeDone(boolean mergeDone) {
		this.mergeDone = mergeDone;
	}
	
}
