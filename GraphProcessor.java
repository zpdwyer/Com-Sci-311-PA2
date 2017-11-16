package PA2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GraphProcessor {
	private HashMap<String, Vertex> mapVertices;				
	private HashMap<Vertex, Boolean> markedVertices;				
	private HashMap<String, ArrayList<String>> outputVertices;	
	private HashMap<Integer, ArrayList<String>> SCCS;		
	private Integer count;								
	private HashMap<String, ArrayList<String>> outputR;	
	private HashMap<String, Integer> timeFinishes;			
	private HashMap<String, Integer> dist;				
	private int sccMaxSize = 0;
	
	public GraphProcessor(String graphData) throws IOException {
		count = 0;
		mapVertices = new HashMap<String, Vertex>();
		initiallyMarked();
		outputVertices = new HashMap<String, ArrayList<String>>();
		outputR = new HashMap<String, ArrayList<String>>();
		fileRead(graphData);    
		timeFinishes = new HashMap<String, Integer>();
		SCCS = new HashMap<Integer, ArrayList<String>>();
		dist = new HashMap<String, Integer>();
		sccComputation();
	}
	
	public int outDegree(String v) {
		ArrayList<String> list = outputVertices.get(v);
		if(list == null){
			return 0;
		}
		return list.size();
	}
	
	public boolean similarComponent(String u, String v) {
		Vertex u2 = mapVertices.get(u);
		Vertex v2 = mapVertices.get(v);
		if(u2 == null || v2 == null) return false;
		return u2.sccKey == v2.sccKey;
	}
	
	public ArrayList<String> bfsPath(String u, String v) {
		ArrayList<String> route = new ArrayList<String>();
		Queue<String> q = new LinkedList<String>();
		ArrayList<String> visitedList = new ArrayList<String>();
		HashMap<String, String> parentNode = new HashMap<String, String>();		
		
		q.add(u);
		visitedList.add(u);
		Vertex k = null;
		
		Iterator<Map.Entry<String, Vertex>> iterator = mapVertices.entrySet().iterator();
	  
		while (iterator.hasNext()) {
	        Map.Entry<String, Vertex> pair = (Map.Entry<String, Vertex>) iterator.next();
	        k = (Vertex) pair.getValue();
	        dist.put(k.toString(), Integer.MAX_VALUE);
	        parentNode.put(k.toString(), null);
	    }
			
		dist.put(u, 0);
		
		while(!q.isEmpty()){
			String next = q.remove();
			if(next.equals(v)){
				break;
			}else{
				ArrayList<String> outEdges = outputVertices.get(next);
				
				if(outEdges != null){
					for(int i = 0; i < outEdges.size(); i++){
						String edge = outEdges.get(i);
						
						if(dist.get(edge) > dist.get(next)+1) {
							dist.put(edge, dist.get(next)+1);
							parentNode.put(edge, next);
						}
						
						if(!visitedList.contains(edge)) {
							q.add(edge);
							visitedList.add(edge);
						}
					}
				}
			}	
		}
	
		route.add(v);
		String temp = parentNode.get(v);
		if(temp == null){
			route.add(v);
			return route;
		}
		
		while(!temp.equals(u)){
			route.add(temp);
			temp = parentNode.get(temp);
		}
		
		route.add(u);
		Collections.reverse(route);
		return route;
	}
	
	private void fileRead(String fileName) throws IOException {
		ArrayList<String> curList = null;
		ArrayList<String> curListR = null;
		String begin = "";
		String end = "";
		String line = "";
		try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();		// read the first line to get to the edges
            
            while((line = br.readLine()) != null) {
            	String[] input = line.split("\\s");
            	begin = input[0];
            	end = input[1];
            	
            	Vertex v = new Vertex(begin);
            	if(mapVertices.get(begin) == null){
            		mapVertices.put(begin, v);
            	}
            	
            	Vertex u = new Vertex(end);
            	if(mapVertices.get(end) == null){
            		mapVertices.put(end, u);
            	}
            	            	
            	curList = outputVertices.get(begin);
				if(curList == null){
					curList = new ArrayList<String>();
				}
				
				if(!curList.contains(end)){
					curList.add(end);
				}
				
				outputVertices.put(begin, curList);
				
				curListR = outputR.get(end);
				if(curListR == null){
					curListR = new ArrayList<String>();
				}
				
				if(!curListR.contains(begin)){
					curListR.add(begin);
				}
				
				outputR.put(end, curListR);
            }
            br.close();         
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();             
        }
		
	}
	
	private void orderComputation() {
		// compute a finish time ordering for SCC algorithm
		initiallyMarked();
		count = 0;
		Vertex vertex = null;
		
		// loop through our HashMap of vertices using an iterator
		Iterator<Map.Entry<String, Vertex>> it = mapVertices.entrySet().iterator();
	  
		while (it.hasNext()) {
	        Map.Entry<String, Vertex> pair = (Map.Entry<String, Vertex>) it.next();
	        vertex = (Vertex) pair.getValue();
	        // perform depth-first search on the graph
	        Boolean mark = markedVertices.get(v.toString());
	        
	        if(mark == null || !mark){
	        	completeDFS(vertex);
	        }
	        
	    }
		
	}
	
	private void completeDFS(Vertex v) {
		// perform recursive DFS on the reversed graph
		markedVertices.put(v, true);
		ArrayList<String> list = outputR.get(v);
		
		if(list != null) {
			for(int i = 0; i < list.size(); i++) {
				String x = list.get(i);
				Vertex z = mapVertices.get(x);
				
				if(!markedVertices.get(z)) {
					completeDFS(z);
				}	
			}
		}
		count++;
		timeFinishes.put(v.toString(), count);
	}
	
	private void sccComputation() {
		orderComputation();
		count = 0;
		timeFinishes = (HashMap<String, Integer>) sortByValue((Map<String, Integer>) timeFinishes); // sort our finishTimes in descending order
		
		initiallyMarked(); // unmark all vertices
				
		Iterator<Map.Entry<String, Integer>> iterator = timeFinishes.entrySet().iterator();
	    String x = null;
	    
		while (iterator.hasNext()) {
	        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) iterator.next();
	        x = (String) pair.getKey();
	        Vertex vertex = mapVertices.get(x);
	        
	        Boolean mark = markedVertices.get(vertex);
	        
	        if(mark == null || !mark) {
	        	ArrayList<String> list = new ArrayList<String>();
	        	list = depthFirstSearchDFS(vertex, list, vertex);
	       
   	        	for(int i = 0; i < list.size(); i++) {
   	        		String str = list.get(i);
        			Vertex vertex2 = mapVertices.get(str);
        			vertex2.sccKey = mapVertices.get(list.get(0)).sccKey;
        			SCCS.put(vertex2.sccKey, list);	
    				if(list.size() > sccMaxSize){
    					sccMaxSize = list.size();
    				}
        				
        		}
	   	        
	        }
	       
		}
		
	}
	
	private ArrayList<String> depthFirstSearchDFS(Vertex v, ArrayList<String> list, Vertex root) {
		
		markedVertices.put(v, true);
		// get all outgoing edges from v (normal graph)
		ArrayList<String> outEdges = outputVertices.get(v.toString());
		Vertex u = null;
		
		if(v != root && !list.contains(v.toString())){
			list.add(v.toString());
		}
		
		if(outEdges != null) {
			for(int i = 0; i < outEdges.size(); i++){

				String w = outEdges.get(i);
				u = mapVertices.get(w);
	
				Boolean mark = markedVertices.get(u);
						
				if(mark == null || !mark) {
					depthFirstSearchDFS(u, list, root);
				}

			}
			if(!list.contains(root.toString())){
				list.add(root.toString());
			}
			
		}
		return list;
	}
	
	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		// sort our finishTimes in descending order for easy iteration
		List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
		
		Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	        @Override
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
	            return ( o2.getValue() ).compareTo( o1.getValue() );
	        }
		});

	    Map<K, V> result = new LinkedHashMap<>();
	    
	    for (Map.Entry<K, V> entry : list) {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    
	    return result;
	}
	
	private void initiallyMarked(){
		markedVertices = new HashMap<Vertex, Boolean>();
		
		for (Map.Entry<Vertex, Boolean> entry : markedVertices.entrySet()) {
		    entry.setValue(false);
		}
		
	}
	
	class Vertex {

		String vertex;
		int sccKey;
		
		public Vertex(String vertex){
			this.vertex = vertex;
			this.sccKey = this.hashCode();
		}
		
		@Override
		public String toString(){
			return vertex;
		}
		
		@Override
		public boolean equals(Object o){
			Vertex v = (Vertex) o;
			return this.vertex.equals(v.vertex);
		}
		
	}
	
}
