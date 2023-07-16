
public class Dbscan 
{
	public static DataPoints[] classify(DataPoints inputData, int clusterCount, int epsilon, int minPoints)
	{
		DataPoints[] clusters = new DataPoints[clusterCount];
		int nextClusterLabel = 0;
		
		for (DataPoint p : inputData)
		{
			if (p.label != Labels.UNDEFINED)	// skip already processed datapoints
				continue;
			
			DataPoints neighbors = rangeQuery(p, inputData, epsilon);
			
			if (neighbors.size() < minPoints)		// p is not a core point.
			{
				p.label = Labels.NOISE;			// p could be a border point, in which case it will get handled later
				continue;
			}
			
			int clusterLabel = nextClusterLabel++;
			DataPoints cluster = new DataPoints();
			
			p.label = clusterLabel;
			cluster.add(p);
			
			for (int i = 0; i < neighbors.size(); i++)	// traditional for loop because we modify cluster while iterating
			{
				DataPoint q = neighbors.get(i);
				
				if (q.label == Labels.NOISE)	// q is a border point, so no need to check q's neighbors
				{
					q.label = clusterLabel;
					cluster.add(q);
					continue;
				}
				if (q.label != Labels.UNDEFINED)	// q already labeled to another cluster (or q == p)
				{
					continue;
				}
				
				q.label = clusterLabel;
				cluster.add(q);
				
				DataPoints indirectNeighbors = rangeQuery(q, inputData, epsilon);
				if (indirectNeighbors.size() >= minPoints)	// expand cluster
				{
					neighbors.addAll(indirectNeighbors);	// probably very inefficient
				}
			}
			
			if (clusterLabel < clusterCount)	
			{
				clusters[clusterLabel] = cluster;
			}
			else	// already created clusterCount clusters - replace smallest cluster
			{
				int smallestClusterIndex = 0;
				int smallestClusterSize = clusters[0].size();
				
				for (int clusterIndex = 1; clusterIndex < clusterCount; clusterIndex++)	// probably not the best approach
				{
					if (clusters[clusterIndex].size() < smallestClusterSize)
					{
						smallestClusterIndex = clusterIndex;
						smallestClusterSize = clusters[clusterIndex].size();
					}
				}
				
				if (smallestClusterSize < cluster.size())
				{
					clusters[smallestClusterIndex] = cluster;
				}
			}
		}
		
		return clusters;
	}
	
	private static DataPoints rangeQuery(DataPoint p, DataPoints dataPoints, int epsilon)
	{
		DataPoints result = new DataPoints();
		
		for (DataPoint q : dataPoints)
		{
			if (distance(p, q) <= epsilon)
				result.add(q);
		}
		
		return result;
	}
	
	private static double distance(DataPoint x, DataPoint y)
	{
		return Math.sqrt(Math.pow(x.xCoord - y.xCoord, 2) + Math.pow(x.yCoord - y.yCoord, 2));
	}
	
	
}
