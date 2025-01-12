/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.graph.communitymining.siwoplus;

import datastructure.core.TimeFrame;
import datastructure.core.graph.classinterface.IDynamicGraph;
import datastructure.core.graph.classinterface.IEdge;
import datastructure.core.graph.classinterface.IGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;

/**
 *
 * @author mahdi
 * @version Jan 2025: Yashar - Updated SIWO, and added Local SIWO.
 */
public class SiwoPlus {
    double __MIN = 0.000001;

    public Graph graph, graphCopy;
    public boolean strengthType, mergeOutliers, detectOverlap;
    public int startingNode;
    public ArrayList<Integer> community;
    public HashSet<Integer> shell;
    public HashSet<Integer> nodesToIgnore;
    public ArrayList<ArrayList<Integer>> partition;
    public HashMap<Integer, HashMap<Integer, Integer>> commonNeighbors;
    public HashMap<Integer, Integer> maxCommonNeighbors;
    public HashSet<Integer> strengthAssignedNodes;
    public HashSet<Integer> processedNodes;
    
    public SiwoPlus(Graph _graph, boolean _strengthType, boolean _mergeOutliers, 
            boolean _detectOverlap, HashSet<Integer> _nodesToIgnore) {
    	this.graph = _graph;
        this.graphCopy = _graph.copy();
    	this.strengthType = _strengthType;
        this.mergeOutliers = _mergeOutliers;
        this.detectOverlap = _detectOverlap;
    	this.startingNode = -1;
    	this.community = new ArrayList<>();
    	this.shell = new HashSet<>();
        this.nodesToIgnore = _nodesToIgnore;
        this.partition = new ArrayList<>();
        this.commonNeighbors = new HashMap<>();
    	this.maxCommonNeighbors = new HashMap<>();
    	this.strengthAssignedNodes = new HashSet<>();
    	this.processedNodes = new HashSet<>();
    	this.removeSelfLoops();
    }
    
    public void reset() {
    	this.community.clear();
    	this.shell.clear();
    }
    
    private void removeSelfLoops() {
        this.graph.nodes().stream().filter((node) -> (this.graph.hasEdge(node, node))).forEachOrdered((node) -> {
            this.graph.removeEdge(node, node);
        });
    }
    
    public void setStartingNode(Node startNode) {
        this.startingNode = startNode.getId();
        this.community.add(startNode.getId());
        startNode.neighbors().keySet().forEach((neighborId) -> {
            this.shell.add(neighborId);
        });
        this.nodesToIgnore.forEach((nodeId) -> {
            this.shell.remove(nodeId);
        });
    }
    
    public void updateSetsWhenNodeJoins(Node node) {
    	this.community.add(node.getId());
    	this.updateShellWhenNodeJoins(node);
    }
    
    public void updateShellWhenNodeJoins(Node node) {
        node.neighbors().keySet().forEach((neighborId) -> {
            this.shell.add(neighborId);
        });
        this.community.forEach((nodeId) -> {
            this.shell.remove(nodeId);
        });
        this.nodesToIgnore.forEach((nodeId) -> {
            this.shell.remove(nodeId);
        });
    }
    
    public void updateDictsOfCommonNeighborsInfo(Node node) {
    	
//    	if a node doesn't exist in one HashSet, it doesn't exist in the other, so we initialize both HashSets for this node.
    	if(this.commonNeighbors.containsKey(node.getId()) == false) {
            this.commonNeighbors.put(node.getId(), new HashMap<>());
            this.maxCommonNeighbors.put(node.getId(), -1);
    	}
    	
    	int numberCommonNeighbors;
    	for(int neighborId : node.neighbors().keySet()) {
    		
//    		these computations are done only if a neighbor is not considered before because (*) below.
            if(this.commonNeighbors.get(node.getId()).containsKey(neighborId) == false) {
                if(this.commonNeighbors.containsKey(neighborId) == false) {
                    this.commonNeighbors.put(neighborId, new HashMap<>());
                    this.maxCommonNeighbors.put(neighborId, -1);
                }

//    			(*) this is the reason, as the number of common neighbors is found, it is assigned for nodes of both ends of that edge.
//    			this makes the total number of calculations in half.
                numberCommonNeighbors = graph.getCommonNeighbors(node, graph.getNode(neighborId)).size();
//                System.out.println(node.getId() + " -> " + neighborId + " : " + numberCommonNeighbors);
                this.commonNeighbors.get(node.getId()).put(neighborId, numberCommonNeighbors);
                this.commonNeighbors.get(neighborId).put(node.getId(), numberCommonNeighbors);

//    			finds the maximum number of common numbers with any neighbor of the node, will be used for normalization in strength formula.
                if(numberCommonNeighbors > this.maxCommonNeighbors.get(node.getId())) {
                    this.maxCommonNeighbors.put(node.getId(), numberCommonNeighbors);
                }
                if(numberCommonNeighbors > this.maxCommonNeighbors.get(neighborId)) {
                    this.maxCommonNeighbors.put(neighborId, numberCommonNeighbors);
                }
            }
    	}
    }
    
    public void assignLocalStrength(Node node) {
    	if(this.strengthAssignedNodes.contains(node.getId()) == false) {
            this.updateDictsOfCommonNeighborsInfo(node);
            int maxMutualNode = this.maxCommonNeighbors.get(node.getId()), maxMutualNeighbor = 0, strength = 0;
//    		once a node is fully analyzed (for its strength values), all edges incident to that can be assigned with proper strength value.
            for(int neighborId : node.neighbors().keySet()) {
                maxMutualNeighbor = this.maxCommonNeighbors.get(neighborId);
                strength = this.commonNeighbors.get(node.getId()).get(neighborId);
    			
                double s1 = 0.0, s2 = 0.0, finalStrength;
    			
                if(maxMutualNode != 0) {
                    s1 = ((double) strength) / maxMutualNode;
                }
                if(maxMutualNeighbor != 0) {
                    s2 = ((double) strength) / maxMutualNeighbor;
                }
    			
//    			a conditional assignment if any of the two formulas is intended.
                finalStrength = (this.strengthType == false) ? (s1 + s2 - 1.0) : ((s1 + s2) / 2.0);
    			
//    			the strength is stored in attributes of both end nodes of an edge.
                node.assignStrength(neighborId, finalStrength);
                this.graph.getNode(neighborId).assignStrength(node.getId(), finalStrength);
            }
    		
//    		adds the analyzed node to this hashset in order to avoid unnecessary calculations in the next rounds.
            this.strengthAssignedNodes.add(node.getId());
    	}
    }
    public HashMap<Integer, Integer> detectFromSeeds(List<Integer> seeds, 
                                                BooleanProperty isThreadRunningProperty) {
        //include neighbors of any degree‐1 seeds
    for (int i = 0; i < seeds.size(); i++) {
        Integer s = seeds.get(i);
        if (this.graph.degree(s) == 1.0) {
            Node seedNode = this.graph.getNode(s);
            if (seedNode != null) {
                for (Integer nbrId : seedNode.neighbors().keySet()) {
                    if (!seeds.contains(nbrId)) {
                        seeds.add(nbrId);
                    }
                }
            }
        }
    }
    
        this.partition.clear();
    this.processedNodes.clear();
    this.nodesToIgnore.clear();
    this.reset(); 
    findCommunity(seeds, isThreadRunningProperty);
    System.out.println("detectFromSeeds found: " + this.community);

    return this.convertPartition();
}
    public HashMap<Integer, Integer> DetectCommunities(BooleanProperty isThreadRunningProperty) {
        System.out.println("Parameters: Str type "+this.mergeOutliers +" Detect overlap " +this.detectOverlap+" Merge Outlier "+this.mergeOutliers);
        while(this.processedNodes.size() < this.graph.getNumberOfNodes()) {
                    HashSet<Integer> remainingNodes = new HashSet<>();
        this.graph.nodes().forEach(node -> {
            if (!this.processedNodes.contains(node.getId())) {
                remainingNodes.add(node.getId());
            }
        });

        // If none left, we are done
        if (remainingNodes.isEmpty()) break;
    int startNodeId = pickHighestDegreeNode(remainingNodes);
            findCommunity(startNodeId, isThreadRunningProperty);

        }
        this.nodesToIgnore.clear();
        if(this.mergeOutliers) {
            this.amendPartition();
        }
        return this.convertPartition();
    }
private int pickHighestDegreeNode(HashSet<Integer> candidateNodes) {
    int bestNode = -1;
    double bestDegree = -1;
    for (Integer nodeId : candidateNodes) {
        double deg = this.graph.degree(nodeId);
        if (deg > bestDegree) {
            bestDegree = deg;
            bestNode = nodeId;
        }
    }
    return bestNode;
}
    public void findCommunity(List<Integer> seeds, BooleanProperty isThreadRunningProperty) {
        this.reset();  
        HashSet<Integer> remainingNodes = new HashSet<>();
        this.graph.nodes().stream().filter((node) -> (this.processedNodes.contains(node.getId()) == false)).forEachOrdered((node) -> {
            remainingNodes.add(node.getId());
        });
       for (Integer seedId : seeds) {
        if (!this.community.contains(seedId)) {
            this.community.add(seedId);
        }
    }

    for (Integer seedId : seeds) {
        Node seedNode = this.graph.getNode(seedId);
        this.assignLocalStrength(seedNode);
        for (Integer nbrId : seedNode.neighbors().keySet()) {
            if (!this.community.contains(nbrId)) {
                this.shell.add(nbrId);
            }
        }
    }
        
        HashMap<Integer, Double> improvements = new HashMap<>();
        
        while(this.community.size() < this.graph.getNumberOfNodes() && this.shell.size() > 0) {
            this.shell.forEach((nodeId) -> {
                this.assignLocalStrength(this.graph.getNode(nodeId));
            });
            int bestNextNode = this.findBestNextNode(improvements);
            if(this.strengthType == false && improvements.get(bestNextNode) < this.__MIN) {
                break;
            }
            if(this.strengthType == true) {
                if(this.community.size() > 3 && improvements.get(bestNextNode) < this.__MIN + 1.0) {
                    break;
                } else if (this.community.size() < 3 && improvements.get(bestNextNode) < this.__MIN) {
                    break;
                }
            }
            this.updateSetsWhenNodeJoins(this.graph.getNode(bestNextNode));
        }
        
        if(this.mergeOutliers==true) {
            this.mergeDanglingNodes();
        }
        
        this.community.forEach((nodeId) -> {
            this.processedNodes.add(nodeId);
        });
        
        if(this.detectOverlap == false) {
            this.community.forEach((nodeId) -> {
                this.nodesToIgnore.add(nodeId);
            });
        }
        System.out.println("Local SIWO found community: " + this.community);
        
        this.partition.add(new ArrayList<>(this.community));
//        this.reset();
    }
    public void findCommunity(int singleSeedId, BooleanProperty isThreadRunningProperty) {
    List<Integer> seeds = new ArrayList<>();
    seeds.add(singleSeedId);
    findCommunity(seeds, isThreadRunningProperty);
}

    public int findBestNextNode(HashMap<Integer, Double> improvements) {
    	int newNodeId = this.community.get(this.community.size() - 1);
        this.shell.forEach((nodeId) -> {
            if(improvements.containsKey(nodeId) == false) {
                improvements.put(nodeId, this.graph.getNode(nodeId).getStrength(newNodeId));
            } else if(this.graph.hasEdge(nodeId, newNodeId)) {
                improvements.put(nodeId, improvements.get(nodeId) + this.graph.getNode(nodeId).getStrength(newNodeId));    			
            }
        });
    	
    	if(improvements.containsKey(newNodeId)) {
            improvements.remove(newNodeId);
    	}
    	
//    	since we made sure this.shell.size() > 0, the final value for bestCandidate cannot remain -1, and will definitely change to a proper value.
    	int bestCandidate = -1;
    	double bestImprovement = Double.NEGATIVE_INFINITY;
    	for(int candidate : this.shell) {
            if(improvements.get(candidate) > bestImprovement) {
                bestCandidate = candidate;
                bestImprovement = improvements.get(candidate);
            }
    	}
    
    	return bestCandidate;
    }
    
    public void mergeDanglingNodes() {
    	HashSet<Integer> neighborhood = new HashSet<>();
        this.community.forEach((Integer nodeId) -> {
            SiwoPlus.this.graph.getNode(nodeId).neighbors().keySet().forEach((neighborId) -> {
                neighborhood.add(neighborId);
            });
            });
    	
//    	to avoid searching through the whole ArrayList, we use a temporary HashSet, then put it back to the this.community.
    	HashSet<Integer> tempCommunity = new HashSet<>(this.community);
        neighborhood.stream().filter((nodeId) -> (this.graph.degree(nodeId) == 1.0)).forEachOrdered((nodeId) -> {
            tempCommunity.add(nodeId);
        });
    	
    	this.community = new ArrayList<>(tempCommunity);
    }
    
    public void amendPartition() {
        ArrayList<ArrayList<Integer>> communities = new ArrayList<>();
        this.partition.stream().filter((com) -> (com.size() == 1 || com.size() == 2)).forEachOrdered((com) -> {
            communities.add(com);
        });
        
        int i = 0;
        while(i < this.partition.size()) {
            if(this.partition.get(i).size() == 1 || this.partition.get(i).size() == 2) {
                this.partition.remove(i);
                i--;
            }
            i++;
        }
        this.amendPartitionHelper(communities);
    }
    
    public void amendPartitionHelper(ArrayList<ArrayList<Integer>> communities) {
        double temp = 0.0;
        for(ArrayList<Integer> com : communities) {
            HashSet<Integer> neighbors = new HashSet<>();
            com.forEach((nodeId) -> {
                this.graph.getNode(nodeId).neighbors().keySet().forEach((neighborId) -> {
                    neighbors.add(neighborId);
                });
            });
            HashMap<Integer, Double> strengths = new HashMap<>();
            for(int neighborId : neighbors) {
                for(int i = 0; i < this.partition.size(); i++) {
                    if(this.partition.get(i).contains(neighborId)) {
                        for(int nodeId : com) {
                            if(this.graphCopy.hasEdge(nodeId, neighborId)) {
                                if(strengths.containsKey(i)) {
                                    temp = strengths.get(i);
                                }
                                strengths.put(i, temp + this.graphCopy.getNode(nodeId).getStrength(neighborId));
                                temp = 0.0;
                            }
                        }
                        break;
                    }
                }
            }
            if(strengths.size() > 0) {
                this.amendPartitionHelper2(com, strengths);            
            } else {
                this.partition.add(new ArrayList<>(com));
            }
        }
    }
    
    public void amendPartitionHelper2(ArrayList<Integer> community, HashMap<Integer, Double> strengths) {
        ArrayList<Integer> strengthsKeys = new ArrayList<>(strengths.keySet());
        int bestCommunityIndex = strengthsKeys.get(0);
        
        for(int strengthKeyIndex : strengthsKeys) {
            if(strengths.get(strengthKeyIndex) > strengths.get(bestCommunityIndex)) {
                bestCommunityIndex = strengthKeyIndex;
            }
        }
        
        for(int nodeId : community) {
            if(this.partition.get(bestCommunityIndex).contains(nodeId) == false) {
                this.partition.get(bestCommunityIndex).add(nodeId);
            }
        }
    }
    
    public HashMap<Integer, Integer> convertPartition() {
        HashMap<Integer, Integer> node2com = new HashMap<>();
        for(int communityIndex = 0; communityIndex < this.partition.size(); communityIndex++) {
            for(int nodeId : this.partition.get(communityIndex)) {
                node2com.put(nodeId, communityIndex);
            }
        }
        return node2com;
    }
    
    
    public static HashMap<Integer, Integer> runSiwoPlus(IDynamicGraph dynaGraph, TimeFrame tf, BooleanProperty isThreadRunningProperty,
            boolean strengthType, boolean mergeOutliers, boolean detectOverlap,ArrayList<Integer> vertexIDs) {        
        Graph louvainGraph = new Graph();
        IGraph meerkatIGraph = dynaGraph.getGraph(tf);
        System.out.println("SIWO PLUS RUN WITH VERTICES "+vertexIDs.toString());
        Map<Integer, Node> mapNodes = new HashMap<>();
        meerkatIGraph.getAllVertexIds().forEach((vid) -> {
            Node newNode = louvainGraph.addNode((Integer)vid);
            mapNodes.put((Integer)vid, newNode);
        });
        
        for( Object vid : meerkatIGraph.getAllVertexIds()){
            List<IEdge> listEdges = dynaGraph.getEdges(meerkatIGraph.getVertex((Integer)vid), tf);
            listEdges.forEach((edge) -> {
                int vertexFrom = edge.getSource().getId();
                int vertexTo = edge.getDestination().getId();
                Node sourceNode = mapNodes.get(vertexFrom);
                Node destinationNode = mapNodes.get(vertexTo);
                if (sourceNode!=null && destinationNode!=null) {
                    louvainGraph.addEdge(sourceNode, destinationNode);
                }
            });
        }
        
        if(isThreadRunningProperty.getValue()==false){
                    return new HashMap<>();
        }
        SiwoPlus CommunityDetector = new SiwoPlus(louvainGraph, strengthType, mergeOutliers, detectOverlap, new HashSet<>());
        
        
        if (vertexIDs == null || vertexIDs.isEmpty()) {
        // Run global SIWO if no query is given
        return CommunityDetector.DetectCommunities(isThreadRunningProperty);
        
        } else {
            // Run multi‐seed SIWO
            return CommunityDetector.detectFromSeeds(vertexIDs, isThreadRunningProperty);

        }
    }
}