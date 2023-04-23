import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Database 
{
	private static class Transaction
	{
		private HashSet<Integer> items;
			
		private Transaction(List<Integer> itemList)
		{
			items = new HashSet<>(itemList);
		}
		
		private boolean contains(Itemset itemset)
		{
			return items.containsAll(itemset);
		}
	}
	
	private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	public static PatternSet allItems = new PatternSet(1);
	
	// cache support of each itemset
	// this seems to actually slow down the support operation, but maybe it'll be beneficial for larger amount of data
	private static HashMap<Itemset, Float> supports = new HashMap<>(); 

	
	private Database() {}
	
	
	// populate database from file
	public static void loadFrom(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		
		String line;
		while ((line = reader.readLine()) != null)
		{
			ArrayList<Integer> transactionItems = new ArrayList<>();
			String split[] = line.split("\t");
			for (String str : split)
			{
				Integer item = Integer.parseInt(str);
				allItems.itemsets.add(new Itemset(item));
				transactionItems.add(item);
			}
			transactions.add(new Transaction(transactionItems));
		}
		
		reader.close();
	}
	
	
	public static float supportOf(Itemset itemset)
	{
		// if memoed
		if (supports.containsKey(itemset))
		{
			return supports.get(itemset);
		}
				
		int frequency = 0;
		
		for (Transaction transaction : transactions)
		{
			if (transaction.contains(itemset))
			{
				frequency++;
			}
		}
		
		float support = ((float) frequency / transactions.size()) * 100f;
		supports.put(itemset, support);
		
		return support;
	}
	
	
	public static float confidenceOf(Itemset x, Itemset y)
	{
		return (supportOf(x.union(y)) / supportOf(x)) * 100f;
	}
	
}
