package PA2;
// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.util.ArrayList;


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
		return 0;
		// implementation
	}

	public ArrayList<String> bfsPath(String u, String v)
	{
		return null;
		// implementation
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

}