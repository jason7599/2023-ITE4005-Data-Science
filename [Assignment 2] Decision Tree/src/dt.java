import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class dt 
{
	private static Feature[] features; 
	private static Feature label; // label doesn't count as a feature
	private static DecisionTree dt;
	
	public static void main(String[] args)
	{
		String trainDataFileName = args[0];
		String testDataFileName = args[1];
		String outputFileName = args[2];
		
		try
		{
			DataPoints trainData = loadTrainData(trainDataFileName);
			dt = DTBuilder.build(features, trainData);

			DataPoints testData = loadTestData(testDataFileName);
			classifyData(testData, outputFileName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static DataPoints loadTrainData(String fileName) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		// load features and label
		String line = reader.readLine();
		String[] featureNames = line.split("\t");
		
		int featureCount = featureNames.length - 1;
		features = new Feature[featureCount];
		for (int i = 0; i < featureCount; i++)
			features[i] = new Feature(featureNames[i]);
		label = new Feature(featureNames[featureCount]);
		
		// load datapoints
		DataPoints dataPoints = new DataPoints();
		while ((line = reader.readLine()) != null)
		{
			String[] split = line.split("\t");
			String[] attributeValues = Arrays.copyOfRange(split, 0, featureCount);
			String labelValue = split[featureCount];
			
			dataPoints.add(new DataPoint(attributeValues, labelValue));
		}
		
		reader.close();
		return dataPoints;
	}
	
	private static DataPoints loadTestData(String fileName) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		String line = reader.readLine(); // first line is redundant
		DataPoints dataPoints = new DataPoints();
		while ((line = reader.readLine()) != null)
		{
			dataPoints.add(new DataPoint(line.split("\t")));
		}
		
		reader.close();
		return dataPoints;
	}
	
	private static void classifyData(DataPoints testData, String fileName) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		for (Feature feature : features)
			writer.write(feature.toString() + '\t');
		writer.write(label.toString() + '\n');
		
		for (DataPoint dataPoint : testData)
			writer.write(dataPoint.toString() + dt.predictLabel(dataPoint) + '\n');
		
		writer.close();
	}
}
