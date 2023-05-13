import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DataPoints extends ArrayList<DataPoint>
{
	public static class Information
	{
		public final float entropy;
		public final String mode;
		
		public Information(float entropy, String mode)
		{
			this.entropy = entropy;
			this.mode = mode;
		}
	}
	
	private Information information; // cache
	
	public Information getInformation()
	{
		if (information == null) // if not memoed
		{
			HashMap<String, Integer> freqMap = new HashMap<>(); 
			
			// count frequency of each class label
			for (DataPoint dataPoint : this)
			{
				String labelValue = dataPoint.getLabelValue();
				Integer freq = freqMap.get(labelValue);
				if (freq == null) freq = 0;
				freqMap.put(labelValue, freq + 1);				
			}
			
			float entropy = 0f;
			int dataCount = size();
			
			String mode = null;
			int bestFreq = 0;
			
			for (Map.Entry<String, Integer> entry : freqMap.entrySet())
			{
				int freq = entry.getValue();
				if (freq >= bestFreq)
				{
					mode = entry.getKey();
					bestFreq = freq;
				}
				
				float p = (float) freq / dataCount;
				entropy += -p * Math.log(p) / Math.log(2);
			}
			
			information = new Information(entropy, mode);
		}
		
		return information;
	}
	
	public HashMap<String, DataPoints> partition(Feature feature)
	{
		HashMap<String, DataPoints> partitions = new HashMap<>();
		
		for (DataPoint dataPoint : this)
		{
			String attributeValue = dataPoint.getValue(feature);
			DataPoints partition;
			if (partitions.containsKey(attributeValue))
			{
				partition = partitions.get(attributeValue);
			}
			else
			{
				partition = new DataPoints();
				partitions.put(attributeValue, partition);
			}
			partition.add(dataPoint);
		}

		return partitions;
	}
}

