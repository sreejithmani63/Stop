package com.purchaseordersequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PurchaseOrderSequence {

public static void main(String args[]) {

List<Order> orders = new ArrayList<>();
orders.add(new Order(7, 6005, 100));
orders.add(new Order(1, 6001, 100));
orders.add(new Order(1, 6001, 103));
orders.add(new Order(6, 6002, 101));
orders.add(new Order(6, 6003, 100));
orders.add(new Order(7, 6003, 101));
orders.add(new Order(7, 6006, 101));

PurchaseOrderSequence purchaseOrderSequence = new PurchaseOrderSequence();
GroupsInfo groupsInfo = purchaseOrderSequence.groupByDestination(orders);
purchaseOrderSequence.populateGroupEndPoints(groupsInfo);
List<Group> destinationGroupList = groupsInfo.getDestinationGroupList();
List<List<Order>> finalOrderList = new ArrayList<>();
List<Order> finalMergedOrderList = new ArrayList<>();
for (int i = 0; i < destinationGroupList.size(); i++) {
Group group = destinationGroupList.get(i);
if (!group.isMergeDone()) {
group.setMergeDone(true);
List<Order> groupOrders = group.getOrders();
// Collections.sort(group.getEndpoints(),
// (e1, e2) -> groupsInfo.getOriginOrder().indexOf(e1) -
// groupsInfo.getOriginOrder().indexOf(e2));
purchaseOrderSequence.sortOrders(groupOrders, group.getEndpoints());
purchaseOrderSequence.mergeOrders(groupOrders, destinationGroupList);
finalOrderList.add(groupOrders);
}
}
finalOrderList.stream().forEach(o -> {

Optional<Order> priorityOrder = o.stream().reduce((order1, order2) -> groupsInfo.getOriginOrder()
.indexOf(order1.getOrigin()) < groupsInfo.getOriginOrder().indexOf(order2.getOrigin()) ? order1 : order2);
if (priorityOrder.isPresent() && (o.indexOf(priorityOrder.get()) >= o.size() / 2)) {
Collections.reverse(o);
}
if (!finalMergedOrderList.isEmpty()
&& groupsInfo.getOriginOrder().indexOf(finalMergedOrderList.get(0).getOrigin()) > groupsInfo
.getOriginOrder().indexOf(o.get(0).getOrigin())) {
finalMergedOrderList.addAll(0,o);
}
else{
finalMergedOrderList.addAll(o);
}
});
finalMergedOrderList.forEach(o-> System.out.println(o.getOrigin() + "-" + o.getDestination()));
}

public void sortOrders(List<Order> orders, List<Integer> endpoints) {
int topEndpoint = endpoints.get(0);
int bottomEndpoint = endpoints.size() == 1 ? topEndpoint : endpoints.get(1);
Collections.sort(orders, (o1, o2) -> {
if (o1.getOrigin() == topEndpoint) {
return -1;
} else if (o2.getOrigin() == topEndpoint) {
return 1;
} else if (o1.getOrigin() == bottomEndpoint) {
return 1;
} else if (o2.getOrigin() == bottomEndpoint) {
return -1;
} else {
return o1.getOrigin() - o2.getOrigin();
}
});
}

public void mergeOrders(List<Order> orders, List<Group> destinationGroupList) {
int ordersListTopEndpoint = orders.get(0).getOrigin();
destinationGroupList.stream().filter(g -> !g.isMergeDone() && g.getEndpoints().contains(ordersListTopEndpoint))
.findFirst().ifPresent(g -> {
g.setMergeDone(true);
Collections.sort(g.getEndpoints(), (e1, e2) -> e1 == ordersListTopEndpoint ? 1 : -1);
sortOrders(g.getOrders(), g.getEndpoints());
orders.addAll(0, g.getOrders());
mergeOrders(orders, destinationGroupList);
});
if (orders.get(orders.size() - 1).getOrigin() != ordersListTopEndpoint) {
int ordersListBottomEndpoint = orders.get(orders.size() - 1).getOrigin();
destinationGroupList.stream()
.filter(g -> !g.isMergeDone() && g.getEndpoints().contains(ordersListBottomEndpoint)).findFirst()
.ifPresent(g -> {
g.setMergeDone(true);
Collections.sort(g.getEndpoints(), (e1, e2) -> e1 == ordersListTopEndpoint ? -1 : 1);
sortOrders(g.getOrders(), g.getEndpoints());
orders.addAll(g.getOrders());
mergeOrders(orders, destinationGroupList);
});
}
}

public GroupsInfo groupByDestination(List<Order> orders) {

Map<Integer, Group> destinationGroupMap = new HashMap<>();
List<Group> destinationGroupList = new ArrayList<>();
Map<Integer, Set<Integer>> originDestinationSetMap = new HashMap<>();
List<Integer> originOrder = new ArrayList<>();
orders.stream().forEach(o -> {
originDestinationSetMap.computeIfAbsent(o.getOrigin(), (origin) -> {
originOrder.add(origin);
return new HashSet<>();
});
originDestinationSetMap.computeIfPresent(o.getOrigin(), (k, destinationSet) -> {
destinationSet.add(o.getDestination());
return destinationSet;
});
destinationGroupMap.computeIfAbsent(o.getDestination(), k -> {
Group group = new Group();
destinationGroupList.add(group);
return group;
});
destinationGroupMap.computeIfPresent(o.getDestination(), (k, g) -> {
g.getOrders().add(o);
return g;
});
});
populateUniqueOrigins(destinationGroupList);
destinationGroupList.sort((g1, g2) -> g1.getUniqueOrigins().size() - g2.getUniqueOrigins().size());
return new GroupsInfo(originDestinationSetMap, destinationGroupList, originOrder);
}

public void populateUniqueOrigins(List<Group> destinationGroupList) {
destinationGroupList.forEach(group -> {
group.getOrders().forEach(o -> {
group.getUniqueOrigins().add(o.getOrigin());
});
});
}

public void populateGroupEndPoints(GroupsInfo groupsInfo) {
List<Group> destinationGroupList = groupsInfo.getDestinationGroupList();
Map<Integer, Set<Integer>> originDestinationSetMap = groupsInfo.getOriginDestinationSetMap();
for (int i = 0; i < destinationGroupList.size(); i++) {
Group destinationGroup = destinationGroupList.get(i);
if (destinationGroup.getUniqueOrigins().size() == 1) {
destinationGroup.getEndpoints().addAll(destinationGroup.getUniqueOrigins());
continue;
} else if (destinationGroup.getUniqueOrigins().size() == 2) {
destinationGroup.getEndpoints().addAll(destinationGroup.getUniqueOrigins());
} else {
for (Integer origin : destinationGroup.getUniqueOrigins()) {
if (originDestinationSetMap.get(origin).size() > 1) {
destinationGroup.getEndpoints().add(origin);
}
if (destinationGroup.getEndpoints().size() > 2) {
throw new InvalidOrderSequenceException("There is an issue with sequencing destinationId "
+ destinationGroup.getOrders().get(0).getDestination());
}
}
}
if (destinationGroup.getEndpoints().size() < 2) {
// if less than 2 origins belong to more than 1 group. In this
// case any unique origin can be selected as an endpoint
destinationGroup.getEndpoints()
.addAll(destinationGroup.getUniqueOrigins().stream()
.filter(origin -> !destinationGroup.getEndpoints().contains(origin))
.sorted((u1,u2)-> groupsInfo.getOriginOrder().indexOf(u1) - groupsInfo.getOriginOrder().indexOf(u2))
.collect(Collectors.toList()).subList(0, 2 - destinationGroup.getEndpoints().size()));
}

validateEndpointsWithPreviousGroups(destinationGroupList.subList(0, i), destinationGroup.getEndpoints());
}
}

private void validateEndpointsWithPreviousGroups(List<Group> destinationGroupList, List<Integer> endpoints) {
int[] count = new int[2];
destinationGroupList.forEach(g -> {
if (g.getEndpoints().size() == 2) {
if (g.getEndpoints().containsAll(endpoints)) {
throw new InvalidOrderSequenceException(
"There are two groups with endpoints : " + endpoints.get(0) + " " + endpoints.get(1));
} else if (g.getEndpoints().contains(endpoints.get(0))) {
count[0]++;
} else if (g.getEndpoints().contains(endpoints.get(1))) {
count[1]++;
}
}
});
if (count[0] == 2) {
throw new InvalidOrderSequenceException(
"There are invalid order sequence for origin : " + endpoints.get(0));
} else if (count[1] == 2) {
throw new InvalidOrderSequenceException(
"There are invalid order sequence for origin : " + endpoints.get(1));
}
}
}
