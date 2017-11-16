package PA2;

public class Edge {
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
		return this.beginning.equals(e.beginning) && this.finish.equals(e.finish);
	}
}
