import java.util.Map;
import java.util.Scanner;

public class DecisionTree 
{
	public final DTNode root = new DTNode();
		
	public String predictLabel(DataPoint dataPoint)
	{
		DTNode node = root;
		while (node.branches != null) // while not leaf
		{
			DTNode branch = node.branches.get(dataPoint.getValue(node.decidingFeature));
			if (branch == null) break; // untrained data branch
			node = branch;
		}
		return node.label;
	}
	
	public void printRules()
	{
		printRulesAux(root);
	}
	
	private void printRulesAux(DTNode node)
	{
		if (node.decidingFeature == null)
		{
			System.out.printf("then class = %s\n", node.label);
			return;
		}
		
		for(Map.Entry<String, DTNode> branch : node.branches.entrySet())
		{
			System.out.printf("if %s %s ", node.decidingFeature, branch.getKey());
			printRulesAux(branch.getValue());
		}
	}

	public void Debug()
	{
		Scanner scan = new Scanner(System.in);
		DTNode focusNode = root;
		String rule = "";
		
		System.out.println("DEBUG MODE\n");
		
		while (true)
		{
			if (rule.isEmpty() == false)
				System.out.println("\nrule = " + rule);
			
			if (focusNode.decidingFeature == null)
			{
				System.out.println("Leaf node reached; label = " + focusNode.label);
				break;
			}
			else
			{
				System.out.println("Deciding feature: " + focusNode.decidingFeature);
				String[] branchNames = new String[focusNode.branches.size()];
				DTNode[] branchNodes = new DTNode[focusNode.branches.size()];
				int i = 0;
				for (Map.Entry<String, DTNode> branch : focusNode.branches.entrySet())
				{
					System.out.printf("%d: %s\n", i, branch.getKey());
					branchNames[i] = branch.getKey();
					branchNodes[i] = branch.getValue();
					i++;
				}
				
				System.out.print("\nSelect branch: ");
				int input = scan.nextInt();
				
				rule += focusNode.decidingFeature.toString() + ' ' + branchNames[input] + ' ';
				focusNode = branchNodes[input];
			}
		}
		
		scan.close();
	}
}
