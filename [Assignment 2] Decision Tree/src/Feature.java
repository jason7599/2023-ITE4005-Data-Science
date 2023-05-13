
public class Feature
{
	private static int featureCount = 0;
	
	private String name;
	public final int index;
	
	public Feature(String name)
	{
		this.name = name;
		index = featureCount++;
	}
	
	public String toString()
	{
		return name;
	}
}