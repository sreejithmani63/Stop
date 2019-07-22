package com.purchaseordersequence;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupsInfo {
	
	private Map<Integer, Set<Integer>> originDestinationSetMap;
	private List<Group> destinationGroupList;
	
	public GroupsInfo(Map<Integer, Set<Integer>> originDestinationSetMap, List<Group> destinationGroupList) {
		super();
		this.originDestinationSetMap = originDestinationSetMap;
		this.destinationGroupList = destinationGroupList;
	}
	public Map<Integer, Set<Integer>> getOriginDestinationSetMap() {
		return originDestinationSetMap;
	}
	public void setOriginDestinationSetMap(Map<Integer, Set<Integer>> originDestinationSetMap) {
		this.originDestinationSetMap = originDestinationSetMap;
	}
	public List<Group> getDestinationGroupList() {
		return destinationGroupList;
	}
	public void setDestinationGroupList(List<Group> destinationGroupList) {
		this.destinationGroupList = destinationGroupList;
	}
	
	
}
