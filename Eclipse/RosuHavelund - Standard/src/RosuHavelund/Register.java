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

public class Register
{
    public Register()
    {

    }

    public Register(ArrayList<SyntaxTree> nodes)
    {
        this.properties = new ArrayList<Property>();

        for (SyntaxTree node : nodes)
        {
            this.properties.add( new Property(node));
        }
    }

    public Property Property(int p)
    {
        return properties.get(p);
    }

    public Property SubProperty(Property parentProperty, int f)
    {
        Property result = null;
        int i = this.properties.size() - 1;

        String subFormula = parentProperty.SubFormula(f);

        while (result == null && i >= 0)
        {
            Property property = this.properties.get(i);

            if (property.Formula().equals(subFormula))
            {
                result = property;
            }

            i--;
        }
        return result;
    }

    public void Evaluate(TraceEvent event, Register next)
    {
        for (int p = this.properties.size() - 1; p >= 0; p--)
        {
            Property property = this.properties.get(p);
            String formula = property.Formula();

            boolean valid = false;

            if (this.EvaluateLiteralFormula(formula))
            {
                valid = property.Formula().equals(event.Value());
            }
            else if (this.EvaluateUnaryFormula(formula, this.AlwaysPattern))
            {
                Property subProperty = this.SubProperty(property, 0);

                valid = subProperty.getValid() && next.Property(p).getValid();
            }
            else if (this.EvaluateUnaryFormula(formula, this.EventuallyPattern))
            {
                Property subProperty = this.SubProperty(property, 0);

                valid = subProperty.getValid() || next.Property(p).getValid();
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.ImpliesPattern))
            {
                Property antecedent = this.SubProperty(property, 0);
                Property conclusion = this.SubProperty(property, 1);

                valid = (!antecedent.getValid() || conclusion.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.AndPattern))
            {
                Property leftProposition = this.SubProperty(property, 0);
                Property rightProposition = this.SubProperty(property, 1);

                valid = (leftProposition.getValid() && rightProposition.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.UntilPattern))
            {
                Property pre = this.SubProperty(property, 0);
                Property post = this.SubProperty(property, 1);

                valid = (post.getValid() || (pre.getValid() && next.Property(p).getValid()));
            }
            else if (this.EvaluateUnaryFormula(formula, this.NotPattern))
            {
                Property subProperty = this.SubProperty(property, 0);

                valid = !subProperty.getValid();
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.OrPattern))
            {
                Property leftProposition = this.SubProperty(property, 0);
                Property rightProposition = this.SubProperty(property, 1);

                valid = (leftProposition.getValid() || rightProposition.getValid());
            }
/*            
            else if (this.EvaluateBinaryOperatorFormula(formula, this.XorPattern))
            {
                Property leftProposition = this.SubProperty(property, 0);
                Property rightProposition = this.SubProperty(property, 1);

                valid = (leftProposition.getValid() ^ rightProposition.getValid());
            }
*/            
            else if (this.EvaluateUnaryFormula(formula, this.NextPattern))
            {
            	Property nextProperty = next.Property(p);
            	Property leftProposition = next.SubProperty(nextProperty, 0); 
                valid = leftProposition.getValid();
            }

            property.setValid(valid);
        }
    }

    public void Update(Register register)
    {
        for (int i = 0; i < this.properties.size(); i++) {
            this.properties.get(i).setValid(register.Property(i).getValid());
        }
    }

    protected ArrayList<Property> properties;

    protected boolean EvaluateUnaryFormula(String formula, String operatorPattern)
    {
        Pattern pattern = Pattern.compile(operatorPattern);
        Matcher matcher = pattern.matcher(formula);

        return (matcher.matches() && this.OperatorOutsideBraces(matcher.start(1), formula));
    	
    }

    protected boolean EvaluateBinaryOperatorFormula(String formula, String operatorPattern)
    {
    	boolean result = false;
    	
    	Pattern pattern = Pattern.compile(operatorPattern);
        Matcher matcher = pattern.matcher(formula);

        while (!result && matcher.find())
        {
    		result = this.OperatorOutsideBraces(matcher.start(), formula);
        }

        return result;
    }

    protected boolean EvaluateLiteralFormula(String formula)
    {
        return formula.length() == 1;
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
	
	protected final String AndPattern = "([^\\&\\&]*)(\\&\\&)(.*)";
	protected final String OrPattern = "([^\\|\\|]*)(\\|\\|)(.*)";
	protected final String XorPattern = "([^\\^]*)(\\^|)(.*)";
	protected final String ImpliesPattern = "([^->]*)(->)(.*)";
	protected final String UntilPattern = "([^U]*)(U)(.*)";
	
	protected final String AlwaysPattern ="(A)(.*)";
	protected final String EventuallyPattern ="(E)(.*)";	
	protected final String NextPattern ="(N)(.*)";
	protected final String NotPattern ="(!)(.*)";
}
