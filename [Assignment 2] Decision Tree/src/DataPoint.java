
public class DataPoint 
{
	private String[] attributeValues;
	private String labelValue;
	
	public DataPoint(String[] values)
	{
		attributeValues = values;
	}
	
	public DataPoint(String[] values, String label)
	{
		attributeValues = values;
		labelValue = label;
	}
	
	public String getValue(Feature feature)
	{
		return attributeValues[feature.index];
	}
	
	public String getLabelValue()
	{
		return labelValue;
	}
	
	public String toString()
	{
		String res = "";
		for (String attributeValue : attributeValues)
			res += attributeValue + '\t';
		return res;
	}
}
