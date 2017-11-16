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
	
	private boolean arraysAreSame(String[] a, String[] b) {
//		System.out.println("In order returned: "+arrayString(b));
//		System.out.println("Should be: "+arrayString(a));
		if (a.length != b.length) {
			return false;
		}
		for(int i = 0; i<a.length; i++) {
			if (a[i].equals(b[i])) {
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
}
