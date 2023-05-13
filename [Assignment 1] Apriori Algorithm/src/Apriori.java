import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Apriori 
{
	public static int minSupport = 5;

	public static void main(String[] args)
	{			
		minSupport = Integer.parseInt(args[0]);
		
		String inputFilePath = args[1];
		String outputFilePath = args[2];

		try
		{
			Database.loadFrom(inputFilePath);
			ArrayList<PatternSet> frequentPatterns = getFrequentPatterns();
			writeAssociationRules(frequentPatterns, outputFilePath);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
	
	private static ArrayList<PatternSet> getFrequentPatterns()
	{
		ArrayList<PatternSet> frequentPatterns = new ArrayList<PatternSet>();
		PatternSet candidates = Database.allItems.prune();
		
		while (candidates.itemsets.isEmpty() == false)
		{
			candidates = candidates.generateNextPatternSet().prune();
			frequentPatterns.add(candidates);
		}
		
		return frequentPatterns;
	}
	
	
	private static void writeAssociationRules(ArrayList<PatternSet> frequentPatterns, String filePath) throws Exception
	{
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		int maxFrequentPatternSize = frequentPatterns.size();
		
		for (int sizeIndex = 0; sizeIndex < maxFrequentPatternSize; sizeIndex++) //
		{
			PatternSet patternSet = frequentPatterns.get(sizeIndex);
			
			for (Itemset itemset : patternSet.itemsets) // for every frequent itemset
			{
				float support = Database.supportOf(itemset);
				
				ArrayList<Itemset> subsets = itemset.getAllSubsets();
				
				for (Itemset subset : subsets)
				{
					// for every itemset, create every x -> y combination
					Itemset associativeItemset = itemset.difference(subset);
					
					writer.write(String.format("%s\t%s\t%.2f\t%.2f\n",
							subset, associativeItemset, support, Database.confidenceOf(subset, associativeItemset)));
				}
			}
		}
		
		writer.close();
	}
		
}
