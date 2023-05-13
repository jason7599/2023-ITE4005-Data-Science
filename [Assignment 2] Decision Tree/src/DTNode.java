import java.util.HashMap;

public class DTNode 
{
	// for branch nodes
	public Feature decidingFeature;
	public HashMap<String, DTNode> branches; // value - node map
	public String label;
}
