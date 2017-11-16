package PA2;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;


public class WikiCrawlerTest {
	
	private void printList (ArrayList<String> list) {
		for (int i=0; i<list.size(); i++) {
			System.out.println(i + ": " + list.get(i));
		}
	}
	
	private void printEdgeList (ArrayList<Edge> list, String file) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			for (int i=0; i<list.size(); i++) {
				pw.write(i + ": " + list.get(i).toString());
				pw.println();
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean arraysAreSame(ArrayList<String> a, ArrayList<String> b) {
//		System.out.println("In order returned: "+arrayString(b));
//		System.out.println("Should be: "+arrayString(a));
		if (a.size() != b.size()) {
			return false;
		}
		for(int i = 0; i<a.size(); i++) {
			if (a.get(i).equals(b.get(i))) {
				continue;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void TestExtractLinks() {
		ArrayList<String> linksList = new ArrayList<String>();
		ArrayList<String> T = new ArrayList<String>();
		T.add("Computer");
		T.add("Atanasoff");
//		T.add("PS4");
		String BASE_URL = "https://en.wikipedia.org";
//		WikiCrawler crawler = new WikiCrawler("/wiki/Fallout_4", 40, T, "out.txt"); // 261
		WikiCrawler crawler = new WikiCrawler("/wiki/Complexity theory", 20, new ArrayList<String>(), "out.txt");
		crawler.crawl();
	}
	
	@Test
	public void TestGraphFromFile() {
		Graph g = new Graph("/Users/KruegerComp/eclipse-workspace/CS311_PA2/testdata.txt");
		assertEquals("Graph has 20 vertices", 20, g.getNumVertices());
		assertEquals("Graph has 112 edges", 112, g.getNumEdges());
	}
	
	@Test
	public void TestOutDegree() {
		GraphProcessor g = new GraphProcessor("/Users/KruegerComp/eclipse-workspace/CS311_PA2/testdata.txt");
		assertEquals("/wiki/Complexity_theory is out degree 19", 19, g.outDegree("/wiki/Complexity_theory"));
		assertEquals("/wiki/Biological_organisation is out degree 1", 1, g.outDegree("/wiki/Biological_organisation"));
		assertEquals("/wiki/blaaa has outdegree 0", 0, g.outDegree("/wiki/blaaa"));
	}
	
	@Test
	public void TestBFS() {
		GraphProcessor g = new GraphProcessor("/Users/KruegerComp/eclipse-workspace/CS311_PA2/testdata2.txt");
		ArrayList<String> z = g.bfsPath("p", "c");
		ArrayList<String> s = new ArrayList<String>();
		s.add("p");
		s.add("a");
		s.add("b");
		s.add("c");
		assertEquals("array is same", true, arraysAreSame(z, s));
		
		GraphProcessor g2 = new GraphProcessor("/Users/KruegerComp/eclipse-workspace/CS311_PA2/testdata.txt");
		ArrayList<String> z2 = g2.bfsPath("/wiki/Attractor", "/wiki/Complex_systems");
		printList(z2);
	}
}
