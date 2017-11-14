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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;




public class WikiCrawler
{
	static final String BASE_URL = "https://en.wikipedia.org";
	private String seedUrl; 
	private int max; 
	private String fileName; 
	private ArrayList<String> topics; 
	// other member fields and methods

	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName)
	{
		this.seedUrl = seedUrl; 
		this.max = max; 
		this.fileName = fileName; 
		this.topics=topics; 
		
		// implementation
	}

	// NOTE: extractLinks takes the source HTML code, NOT a URL
	public ArrayList<String> extractLinks(String doc)throws IOException { 
	{
		ArrayList<String> linksList = new ArrayList<String>();	
		String[] lines = doc.split("\r");
		
		for(int i=0; i< lines.length; i++)
		{
			String regex = "href=\"wiki/.*?\""; 
			Pattern str = Pattern.compile(regex); 
			Matcher matcher = str.matcher(lines[i]); 
			while(matcher.find()){
				String link = matcher.group().substring(matcher.group().indexOf('"')+1, matcher.group().length()-1); 
				if(!linksList.contains("#") && !linksList.contains(":") && !linksList.contains(link)){
					linksList.add(link); 
				}
			}
			if(!topics.isEmpty())
			{
				
			}
		}
		return linksList; 
		}
	}
	public void crawl()throws MalformedURLException, IOException, InterruptedException{
		{
			// implementation
			ArrayList<Edge> edgeList = traverseBFS(seedUrl);
			String fileContent = "";
			for(int i=0; i<edgeList.size(); i++){
				fileContent = fileContent + edgeList.get(i).toString() + "\r"; 
			}
			BufferedWriter bWriter = null;
			FileWriter fWriter = null;
			try {
				fWriter = new FileWriter(fileName); 
				bWriter = new BufferedWriter(fWriter); 
				bWriter.write(max + "\n"); 
				bWriter.write(fileContent); 
			}catch (IOException e){
				e.printStackTrace(); 
			}finally { 
				try {
					if(bWriter != null)
						bWriter.close(); 
					if(fWriter !=null)
						fWriter.close(); 
				}catch (IOException ex) { 
					ex.printStackTrace(); 
				}
			}
		}	
	}
	
	private ArrayList<Edge> traverseBFS(String seed_url) throws InterruptedException, IOException{
		{
			Queue<String> q = new LinkedList<String>();
			ArrayList<String> visited = new ArrayList<String>();
			ArrayList<String> extractedLinks = new ArrayList<String>();
			ArrayList<Edge> edgeList = new ArrayList<Edge>();

			InputStream inputStream = null;
			BufferedReader bufferReader = null;
			StringBuffer returned = null;
			
			q.add(seed_url);
			visited.add(seed_url);
			
			int counter = 1;
			
			while(!q.isEmpty()){
				String curPage = q.poll();
				boolean linkParse = false;
				
				try {	    
					inputStream = new URL(BASE_URL + curPage).openStream();
				    bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				    returned = new StringBuffer();
				    counter++;
				} catch (Exception e) {
			      e.printStackTrace();
				}
						
				if(counter % 100 == 0){
					Thread.sleep(3000);
				}
			    String line;
			    while ((line = bufferReader.readLine()) != null) {
			    	if(line.contains("<p>") || line.contains("<P>")){
			    		linkParse = true;
			    	}
			    	if(linkParse){
			    		returned.append(line);
			    		returned.append('\r');
			    	}
			    }
			    extractedLinks = extractLinks(returned.toString());  
			    for(int i = 0; i < extractedLinks.size(); i++){
			    	String link = extractedLinks.get(i);
			    	if(!visited.contains(link) && visited.size() < max) {
			    		q.add(link);
			    		visited.add(link);
		    		}
			    	Edge edge = new Edge(curPage, link);
			    	if(visited.contains(link) && !curPage.equals(link) && !edgeList.contains(edge)){
		    			edgeList.add(edge);
		    		}	
			    }
			   if(bufferReader != null)
				   bufferReader.close();
			   if(inputStream != null)
				   inputStream.close();
			}
			return edgeList;
		}
	}
	
	class Edge {
		String beginning;
		String finish;
		
		public Edge(String beginning, String finish){
			this.beginning = beginning;
			this.finish = finish;
		}	
		
		@Override
		public String toString(){
			return beginning + " " + finish;
		}
		
		@Override
		public boolean equals(Object obj){
			Edge e = (Edge) obj;
			return this.beginning == e.beginning && this.finish == e.finish;
		}
	}
}



