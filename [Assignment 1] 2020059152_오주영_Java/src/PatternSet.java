import java.util.HashSet;
import java.util.Iterator;

public class PatternSet 
{
	private int patternSize;
	public HashSet<Itemset> itemsets;
	
	public PatternSet(int size)
	{
		patternSize = size;
		itemsets = new HashSet<>();
	}

	public PatternSet prune()
	{
		Iterator<Itemset> i = itemsets.iterator();
		
		while (i.hasNext())
		{
			Itemset itemset = i.next();
			
			if (Database.supportOf(itemset) < Apriori.minSupport)
			{
				i.remove();
			}
		}
		
		return this; // builder pattern?
	}
	
	public PatternSet generateNextPatternSet()
	{
		int nextPatternSize = patternSize + 1;
		PatternSet nextPatternSet = new PatternSet(nextPatternSize);
		
		Itemset[] itemsetArray = itemsets.toArray(new Itemset[0]); // to make use of indexing
		int itemsetCount = itemsetArray.length;
		
		for (int i = 0; i < itemsetCount; i++)
		{
			for (int j = i + 1; j < itemsetCount; j++)
			{
				Itemset combination = itemsetArray[i].union(itemsetArray[j]); // self join
				
				if (combination.size() != nextPatternSize) continue;
				
				Itemset subset = new Itemset(combination);  // check every k - 1 size subset
				boolean subsetTest = true;
				
				for (Integer item : combination)
				{
					subset.remove(item);
					
					if (itemsets.contains(subset) == false) // pruning "before" generation
					{
						subsetTest = false;
						break;
					}
					
					subset.add(item);
				}
				
				if (subsetTest)
				{
					nextPatternSet.itemsets.add(combination);
				}
			}
		}
		
		return nextPatternSet;
	}
}
