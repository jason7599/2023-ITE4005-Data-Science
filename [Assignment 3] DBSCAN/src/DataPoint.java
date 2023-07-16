
public class DataPoint 
{
	public final int id;
	public final double xCoord;
	public final double yCoord;
	public int label = Labels.UNDEFINED;
	
	public DataPoint(int id, double x, double y)
	{
		this.id = id;
		xCoord = x;
		yCoord = y;
	}
}
