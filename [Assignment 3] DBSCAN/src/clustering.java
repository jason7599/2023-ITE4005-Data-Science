import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class clustering 
{
	public static void main(String[] args)
	{
		String inputFileName = args[0];
		int clusterCount = Integer.parseInt(args[1]);
		int epsilon = Integer.parseInt(args[2]);
		int minPoints = Integer.parseInt(args[3]);
				
		try
		{
			DataPoints inputData = loadDataPoints(inputFileName);
			
			DataPoints[] clusters = Dbscan.classify(inputData, clusterCount, epsilon, minPoints);
			String outputFileNameBase = inputFileName.split("\\.")[0] + "_cluster_";
			
			for (int clusterIndex = 0; clusterIndex < clusters.length; clusterIndex++)
				writeCluster(clusters[clusterIndex], outputFileNameBase + clusterIndex + ".txt");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
	}
	
	private static DataPoints loadDataPoints(String fileName) throws Exception
	{
		DataPoints dataPoints = new DataPoints();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		
		while ((line = reader.readLine()) != null)
		{
			String[] dataInfo = line.split("\t");
			int id = Integer.parseInt(dataInfo[0]);
			double x = Double.parseDouble(dataInfo[1]);
			double y = Double.parseDouble(dataInfo[2]);
			dataPoints.add(new DataPoint(id, x, y));
		}
		
		reader.close();
		return dataPoints;
	}
	
	private static void writeCluster(DataPoints cluster, String fileName) throws Exception
	{
		if (cluster == null)
		{
			System.out.println("cluster for " + fileName + " is null!");
			return;
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		for (DataPoint p : cluster)
			writer.write(p.id + "\n");
		writer.close();
	}
}
