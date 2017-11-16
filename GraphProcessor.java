package PA2;
// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class GraphProcessor
{
	private Graph graph;

	// NOTE: graphData should be an absolute file path
	public GraphProcessor(String graphData)
	{
		graph = new Graph(graphData);
	}

	public int outDegree(String v)
	{
		int outwardEdgeCount = 0;
		for (int i=0; i<graph.getNumEdges(); i++) {
			if (graph.getEdge(i).beginning.equals(v)) {
				outwardEdgeCount++;
			}
		}
		return outwardEdgeCount;
	}

	public ArrayList<String> bfsPath(String u, String v)
	{
		HashMap<String, Boolean> visited = new HashMap<String,Boolean>();
//		HashMap<String, Integer> distance = new HashMap<String,Integer>();
        Queue<BfsLinkedList<String>> q = new LinkedList<BfsLinkedList<String>>();
        
        visited.put(u, true);
//        distance.put(u, 0);
        
		q.add(new BfsLinkedList<String>(u));
 
        while (!q.isEmpty())
        {
        		BfsLinkedList<String> pointer = q.poll();
 
            // queue all unvisited adjacent vertices of pointer
            for (int i=0; i<graph.getNumEdges(); i++) {
            		Edge e = graph.getEdge(i);
            		if (e.beginning.equals(pointer.value) && visited.get(e.finish) == null) {
            			visited.put(e.finish, true);
//            			distance.put(e.finish, distance.get(e.beginning)+1);
            			BfsLinkedList<String> newChild = new BfsLinkedList<String>(e.finish);
            			newChild.parent = pointer;
            			q.add(newChild);
            			if(newChild.value.equals(v)) {
            				ArrayList<String> path = new ArrayList<String>();
            				while (newChild != null) {
            					path.add(0, newChild.value);
            					newChild = newChild.parent;
            				}
            				return path;
            			}
            		}
            }
        }
        return new ArrayList<String>();
	}

	public int diameter()
	{
		return 0;
		// implementation
	}

	public int centrality(String v)
	{
		return 0;
		// implementation
	}
	
	private class BfsLinkedList<T> {
		BfsLinkedList<T> parent = null;
		T value = null;
		
		public BfsLinkedList(T value) {
			this.value = value;
		}
	}

}