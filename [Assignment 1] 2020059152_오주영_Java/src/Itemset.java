import java.util.ArrayList;
import java.util.HashSet;

@SuppressWarnings("serial")
public class Itemset extends HashSet<Integer> 
{
	public Itemset()
	{
		super();
	}
	
	public Itemset(Integer item)
	{
		super.add(item);
	}
	
	public Itemset(Itemset other)
	{
		super(other); // copy constructor
	}

	public Itemset union(Itemset other)
	{
		Itemset copy = new Itemset(this);
		copy.addAll(other);
		return copy;
	}
	
	public Itemset difference(Itemset other)
	{
		Itemset copy = new Itemset(this);
		copy.removeAll(other);
		return copy;
	}
	
	public String toString()
	{
		String res = super.toString().replaceAll(" ", "");
		return '{' + res.substring(1, res.length() - 1) + '}';
	}
	
	public ArrayList<Itemset> getAllSubsets()
	{
		ArrayList<Itemset> subsets = new ArrayList<>();
		if (this.size() > 1)
		{
			ArrayList<Integer> itemList = new ArrayList<>(this);
			combination(itemList, new Itemset(), subsets, 0);			
		}
		return subsets;
	}
	
	
	// Backtracking. get all size 1 ~ k-1 subsets
	private void combination(ArrayList<Integer> itemList, Itemset currentSubset, ArrayList<Itemset> out, int index)
	{
		
		if (currentSubset.size() == itemList.size() - 1) // size k-1 reached
		{
			out.add(new Itemset(currentSubset));
			return;
		}
		
		if (index == itemList.size()) // finish
		{
			if (currentSubset.isEmpty() == false)
			{
				out.add(new Itemset(currentSubset));
			}
			return;
		}
		
		combination(itemList, currentSubset, out, index + 1); // without adding item in this index
		
		
		Integer currentItem = itemList.get(index);
		
		currentSubset.add(currentItem);
		combination(itemList, currentSubset, out, index + 1); // with adding item in this index
		currentSubset.remove(currentItem);
		
	}
	
}