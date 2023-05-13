import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DTBuilder 
{
	private DTBuilder() {}

	private static HashSet<Feature> featureSet; // for recursive building
	
	public static DecisionTree build(Feature[] features, DataPoints dataPoints)
	{
		featureSet = new HashSet<Feature>(Arrays.asList(features));
		DecisionTree decisionTree = new DecisionTree();
		growNode(decisionTree.root, dataPoints);
		return decisionTree;
	}
	
	private static void growNode(DTNode node, DataPoints dataPoints)
	{	
		DataPoints.Information info = dataPoints.getInformation();
		node.label = info.mode; // majority voting. 
		// Even if this node is not a leaf, might have to use the label of this node if the branch for a given test datapoint does not exist

		// Terminate recursion if perfectly classified or no features left
		if (featureSet.isEmpty() || info.entropy == 0f) 
		{
			return;
		}
		
		// select best feature
		Feature decidingFeature = null;
		HashMap<String, DataPoints> bestPartitions = null;
		float bestGainRatio = 0f;
		
		for (Feature feature : featureSet)
		{
			HashMap<String, DataPoints> partitions = dataPoints.partition(feature);
			
			float newEntropy = 0f; // Info after split
			float splitInfo = 0f;
			
			for (DataPoints partition : partitions.values())
			{
				float sizeRatio = (float) partition.size() / dataPoints.size();
				newEntropy += sizeRatio * partition.getInformation().entropy;
				splitInfo += -sizeRatio * Math.log(sizeRatio) / Math.log(2);
			}
			
			float gainRatio = (info.entropy - newEntropy) / splitInfo;
			
			if (gainRatio >= bestGainRatio)
			{
				decidingFeature = feature;
				bestPartitions = partitions;
				bestGainRatio = gainRatio;
			}
		}
		
		featureSet.remove(decidingFeature); // backtracking
		
		node.decidingFeature = decidingFeature;
		node.branches = new HashMap<>();
		
		for (Map.Entry<String, DataPoints> partition : bestPartitions.entrySet())
		{
			DTNode child = new DTNode();
			node.branches.put(partition.getKey(), child);
			growNode(child, partition.getValue());
		}
	
		featureSet.add(decidingFeature);
	}
}
