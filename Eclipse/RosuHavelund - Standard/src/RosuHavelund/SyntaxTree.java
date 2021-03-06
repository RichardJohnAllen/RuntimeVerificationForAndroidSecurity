/*
===============================================================================

Rosu-Havelund algorithm.
An ad-hoc implemenation that interprets and evaluates LTL formulae
over a finite trace.

===============================================================================

Copyright (C) 2021  Richard John Allen

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

===============================================================================
*/

package RosuHavelund;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxTree
{
    private String formula;
    private SyntaxTree parent;
    private ArrayList<SyntaxTree> subTrees;

    public SyntaxTree(String formula)
    {
        this(formula, null);
    }

    public SyntaxTree(String formula, SyntaxTree parent)
    {
        this.formula = this.StripOuterBraces(formula.trim());
        this.subTrees = new ArrayList<SyntaxTree>();

        if (parent != null)
        {
            this.parent = parent;
            this.parent.Subtrees().add(this);
        }

        if (this.formula.length() > 1)
        {
            this.AddSubtrees(this.formula, this);
        }
    }

    public String Formula()
    {
        return this.formula;
    }

    public SyntaxTree Parent()
    {
        return this.parent;
    }

    public ArrayList<SyntaxTree> Subtrees()
    {
        return this.subTrees;
    }

    public ArrayList<SyntaxTree> Flatten()
    {
        ArrayList<SyntaxTree> flat = new ArrayList<SyntaxTree>();
        this.FlattenBreadthFirst(this, flat);

        return flat;
    }

    private void AddSubtrees(String formula, SyntaxTree parent)
    {
    	boolean hasSubTrees =
    			this.AddBinaryOperatorFormula(formula, parent, this.AndPattern) ||
    			this.AddBinaryOperatorFormula(formula, parent, this.OrPattern) ||
    			this.AddBinaryOperatorFormula(formula, parent, this.XorPattern) ||
    			this.AddBinaryOperatorFormula(formula, parent, this.ImpliesPattern) ||
    			this.AddBinaryOperatorFormula(formula, parent, this.UntilPattern) ||
    			this.AddUnaryOperatorFormula(formula, parent, this.AlwaysPattern) ||
    			this.AddUnaryOperatorFormula(formula, parent, this.EventuallyPattern) ||
    			this.AddUnaryOperatorFormula(formula, parent, this.NextPattern) ||
    			this.AddUnaryOperatorFormula(formula, parent, this.NotPattern);
    }

    private boolean AddUnaryOperatorFormula(String formula, SyntaxTree parent, String operatorPattern)
    {
    	boolean result = false;
    	
        Pattern pattern = Pattern.compile(operatorPattern);
        Matcher matcher = pattern.matcher(formula);

        if (matcher.matches() && this.OperatorOutsideBraces(matcher.start(1), formula))
        {
        	new SyntaxTree(matcher.group(2), parent);
        	result = true;
        }

        return result;
    }
    
    private boolean AddBinaryOperatorFormula(String formula, SyntaxTree parent, String operator)
    {
    	boolean result = false;
    	
    	Pattern pattern = Pattern.compile(operator);
        Matcher matcher = pattern.matcher(formula);

        while (!result && matcher.find())
        {
    		result = this.OperatorOutsideBraces(matcher.start(), formula);
        }

    	if (result)
    	{
        	String leftOperand = formula.substring(0, matcher.start());
        	String rightOperand = formula.substring(matcher.end(), formula.length());

        	new SyntaxTree(leftOperand, parent);
        	new SyntaxTree(rightOperand, parent);
    	}

        return result;
    }

    private String StripOuterBraces(String formula)
    {
    	String result = formula;

    	if (result.startsWith("("))
    	{
    		result = result.substring(1);
    	}
    	
    	if (result.endsWith(")"))
		{
    		result = result.substring(0, result.length() - 1);
		}
    	
    	return result;
    }
	
	private boolean OperatorOutsideBraces(int pos, String formula)
	{
		int braceCount = 0;
		
		for (int i = 0; i < pos; i++)
		{
			switch (formula.charAt(i))
			{
				case '(':
					braceCount++;
					break;
				case ')':
					braceCount--;
					break;
			}
		}
		
		return braceCount == 0;
	}
	
    private void FlattenBreadthFirst(SyntaxTree tree, ArrayList<SyntaxTree> nodes)
    {
    	nodes.add(tree);

    	int i = nodes.size() - 1;
    	
    	while (i < nodes.size())
    	{
        	SyntaxTree node = nodes.get(i);
        	
            for (SyntaxTree subtree : node.Subtrees())
            {
                nodes.add(subtree);
            }
            
            i++;
    	}
    }
    
	private final String AndPattern = "&&"; 
	private final String OrPattern = "\\|\\|";
	private final String XorPattern = "\\^";
	private final String ImpliesPattern = "->";
	private final String UntilPattern = "U";
	
	private final String AlwaysPattern ="(A)(.*)";
	private final String EventuallyPattern ="(E)(.*)";	
	private final String NextPattern ="(N)(.*)";
	private final String NotPattern ="(!)(.*)";
}
