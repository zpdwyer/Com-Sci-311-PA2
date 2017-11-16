package PA2;
// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.*;



public class WikiCrawler
{
	static final String BASE_URL = "https://en.wikipedia.org";
	private String seedUrl; 
	private int max; 
	public String fileName; 
	private ArrayList<String> topics; 
	private Graph graph = new Graph();
	int requestCount = 0;

	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName)
	{
		this.seedUrl = seedUrl; 
		this.max = max; 
		this.fileName = fileName; 
		this.topics=topics; 
		
	}
	
	public void crawl() {

		traverseBFS(seedUrl);
		String fileContent = "";
		for(int i=0; i<graph.edgeList.size(); i++){
			fileContent = fileContent + graph.edgeList.get(i).toString() + "\r"; 
		}
		BufferedWriter bWriter = null;
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(fileName); 
			bWriter = new BufferedWriter(fWriter); 
			bWriter.write(max + "\n"); 
			bWriter.write(fileContent); 
			if(bWriter != null)
				bWriter.close(); 
			if(fWriter !=null)
				fWriter.close(); 
		} catch (IOException e){
			e.printStackTrace(); 
		}
		
	}
	
	public void traverseBFS(String seed_url) {
		{
			
			Queue<String> q = new LinkedList<String>();
			ArrayList<String> visited = new ArrayList<String>();
			
			q.add(seed_url);	
			
			while(!q.isEmpty()){
				String curPage = q.poll();
			    
				String doc = getHTML(BASE_URL + curPage);
				doc = getTextAfterPTag(doc);
				
				if (curPage.equals(seed_url)) {
					if (hasTopics(seed_url, doc) == false) 
						return;
				}
				// pattern for links
				String regex = "href=\"/wiki/.*?\""; 
				
				Pattern str = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); 
				Matcher matcher = str.matcher(doc); 

				// searching the html content...
				while(matcher.find()){
					// trim away the href="_____"
					String link = doc.substring(matcher.start()+6, matcher.end()-1);
					boolean linkContainsAllTopics = false;;
					if(graph.valueList.size() < max) {
						linkContainsAllTopics = hasTopics(link, doc);
					}
					if (linkContainsAllTopics) {
						if (!link.contains("#") && !link.contains(":") && !link.equals(curPage)) {
							// check if link is valid/ not discovered
							if (!graph.valueList.contains(link)  &&  graph.valueList.size() < max) {
								graph.valueList.add(link);
								q.add(link);
							}
							// check if edge already exists
							if (!graph.edgeList.contains(new Edge(curPage,link)) && graph.valueList.contains(link)) {
								graph.edgeList.add(new Edge(curPage, link));
							}
						} 
					}
					else {
						// check if edge already exists
						if (!graph.edgeList.contains(new Edge(curPage,link)) && graph.valueList.contains(link) && !link.equals(curPage)) {
							graph.edgeList.add(new Edge(curPage, link));
						}
					}
				}
				visited.add(curPage);
			}
		}
	}
	
	public boolean hasTopics (String link, String doc) {
		if (topics.size() == 0)
			return true;
		
		// add patterns for all topics
		String regex = ""; 
		regex += topics.get(0);
		for (int i=1; i<topics.size(); i++) {
			regex += ("|" + topics.get(i)) ;
		}
		Pattern str = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); 
		Matcher matcher = str.matcher(doc); 
		
		// each index in topicOccurance corresponds to the index of a topic in topics
		// i.e. (topicOccurance[i] == true) means that topics.get(i) was found
		Boolean[] topicOccurance = new Boolean[topics.size()];

		// searching the html content...
		while(matcher.find()){
			String match = doc.substring(matcher.start(), matcher.end());
			// check if match is one of the needed topics
			for (int i=0; i<topics.size(); i++) {
				if (topics.get(i).equalsIgnoreCase(match)) {
					// mark topic as found if not already
					topicOccurance[i] = true;
				}
			}
		}
		
		// check if all topics were found
		int numTopicsFound = 0;
		for (int i=0; i<topicOccurance.length; i++) {
			if (topicOccurance[i] == null) 
				return false;
		}
		
		return true;
	}
	
	private String getHTML(String link) {
		String htmlContent = "";
        try {
            URL url = new URL(link);
            URLConnection UrlConnection = url.openConnection();

            BufferedReader br = new BufferedReader(
                               new InputStreamReader(UrlConnection.getInputStream()));

            String htmlLine;
            while ((htmlLine = br.readLine()) != null) {
                    htmlContent += htmlLine;
            }
            br.close();
            requestCount ++;
            if (requestCount >= 50) {
            		try {
						Thread.sleep(5000);
						requestCount = 0;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return htmlContent;
        
	}
	
	private String getTextAfterPTag (String html) {
		int indexLowerP = html.indexOf("<p");
		int indexUpperP = html.indexOf("<P");
		if (indexLowerP == -1 && indexUpperP == -1)
			return "";
		else if (indexLowerP == -1)
			return html.substring(indexUpperP);
		else if (indexUpperP == -1)
			return html.substring(indexLowerP);
		else if (indexLowerP < indexUpperP) 
			return html.substring(indexLowerP);
		else
			return html.substring(indexUpperP);
	}
	
	
}



