package PA2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
	private int numVertices;
	private int numEdges;
	// hashmap for speedy lookups?
	private ArrayList<String> valueList = new ArrayList<String>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();
	
	public Graph() {}

	public Graph(String filename) {
		importFromFile(filename);
	}
	
	private void importFromFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    while ((line = br.readLine()) != null) {
		    		String from = line.substring(0, line.indexOf(' '));
		    		String to = line.substring(line.indexOf(' ')+1, line.length());
		    		// add the edge
		    		this.addEdge(new Edge(from, to));
		    		// add the vertex if not already in list
		    		if (!this.containsVertex(from))
		    			this.addVertex(from);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getNumVertices() {
		return numVertices;
	}
	
	public boolean containsVertex(String value) {
		return valueList.contains(value);
	}
	
	public void addVertex(String value) {
		valueList.add(value);
		numVertices++;
	}
	
	public int getNumEdges() {
		return numEdges;
	}

	public Edge getEdge(int index) {
		return edgeList.get(index);
	}
	
	public boolean containsEdge (Edge e) {
		return edgeList.contains(e);
	}

	public void addEdge(Edge e) {
		edgeList.add(e);
		numEdges++;
	}
	
	public void removeEdge(int index) {
		edgeList.remove(index);
		numEdges--;
	}
	
}

