/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.graph.communitymining;

import algorithm.graph.communitymining.siwoplus.SiwoPlus;
import java.util.List;
import datastructure.core.TimeFrame;
import datastructure.core.graph.classinterface.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mahdi
 * @param <V>
 * @param <E>
 */
public class SiwoPlusMiner<V extends IVertex, E extends IEdge<V>>
        extends Miner<V, E> {

    public static final String STR_NAME = "SiwoPlus";
    public static boolean strengthType; 
    public static boolean mergeOutliers;
    public static boolean detectOverlap;
    public static ArrayList<Integer> vertexIDs = new ArrayList<>();
    
    public SiwoPlusMiner(IDynamicGraph<V, E> pIGraph, TimeFrame tf, String[] parameters) {
        super(pIGraph, tf);
        System.out.println("PARAMETER 3: "+ parameters[3]);
        strengthType = parameters[0].contains("2");
        detectOverlap = parameters[1].contains("true");
        mergeOutliers = parameters[2].contains("true");
        System.out.println(String.valueOf(strengthType) + " " +
                String.valueOf(mergeOutliers) + " " +
                String.valueOf(detectOverlap));
        String vertexIDs_str = parameters[3].substring("VertexIDs:[".length(),parameters[3].length()-1);
        String[] numberStrs = vertexIDs_str.isEmpty() ? new String[0] : vertexIDs_str.split(",");

        
        vertexIDs.clear();
        for(int i = 0;i < numberStrs.length;i++){
            vertexIDs.add(Integer.parseInt(numberStrs[i].trim()));}
        System.out.println(vertexIDs_str);

        System.out.println(vertexIDs);
        
        
        
    }

    @Override
    public void run() {
        System.out.println("********* SiwoPlus ++++++++");
        
        mineGraph(strengthType, mergeOutliers, detectOverlap,vertexIDs);
        if(!running){
            return;
        }
        updateDataStructure();
        blnDone = true;
    }
    
    public void mineGraph(boolean strengthType, boolean mergeOutliers, boolean detectOverlap,ArrayList<Integer> vertexIDs) {
//        HashMap<Integer, Integer> results = new HashMap<>();
        HashMap<Integer, Integer> results = SiwoPlus.runSiwoPlus(dynaGraph, tf, this.isThreadRunningProperty, strengthType, mergeOutliers, detectOverlap,vertexIDs);
        if (!running) {
            return;
        }
        results.keySet().forEach((vertexId) -> {
            int communityId = results.get(vertexId);
            if (hmpCommunities.containsKey(communityId+"")) {
                hmpCommunities.get(communityId+"").add(vertexId);
            } else {
                List<Integer> listofVertices = new ArrayList<>();
                listofVertices.add(vertexId);
                hmpCommunities.put(communityId+"", listofVertices);
            }
        });
    }
    
    @Override
    public String toString() {
        return STR_NAME;
    }
}
