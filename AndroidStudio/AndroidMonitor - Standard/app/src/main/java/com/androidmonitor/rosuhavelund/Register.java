// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

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
            this.properties.add(new Property(node));
        }
    }

    public Property Property(int p)
    {
        return properties.get(p);
    }

    public Property FindSubProperty(Property parentProperty, int f)
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

    public boolean Evaluate(TraceEvent event, Register next)
    {
        boolean result = false;

        for (int p = this.properties.size() - 1; p >= 0; p--)
        {
            Property property = this.properties.get(p);
            String formula = property.Formula();

            boolean satisfied = false;

            if (this.EvaluateLiteralFormula(formula))
            {
                satisfied = property.Formula().equals(event.Event());
            }
            else if (this.EvaluateUnaryFormula(formula, this.AlwaysPattern))
            {
                Property subProperty = this.FindSubProperty(property, 0);

                satisfied = subProperty.getSatisfied() && next.Property(p).getSatisfied();
            }
            else if (this.EvaluateUnaryFormula(formula, this.EventuallyPattern))
            {
                Property subProperty = this.FindSubProperty(property, 0);

                satisfied = subProperty.getSatisfied() || next.Property(p).getSatisfied();
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.ImpliesPattern))
            {
                Property antecedent = this.FindSubProperty(property, 0);
                Property conclusion = this.FindSubProperty(property, 1);

                satisfied = (!antecedent.getSatisfied() || conclusion.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.AndPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                satisfied = (leftProposition.getSatisfied() && rightProposition.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.UntilPattern))
            {
                Property pre = this.FindSubProperty(property, 0);
                Property post = this.FindSubProperty(property, 1);

                satisfied = (post.getSatisfied() || (pre.getSatisfied() && next.Property(p).getSatisfied()));
            }
            else if (this.EvaluateUnaryFormula(formula, this.NotPattern))
            {
                Property subProperty = this.FindSubProperty(property, 0);

                satisfied = !subProperty.getSatisfied();
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.OrPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                satisfied = (leftProposition.getSatisfied() || rightProposition.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.XorPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                satisfied = (leftProposition.getSatisfied() ^ rightProposition.getSatisfied());
            }
            else if (this.EvaluateUnaryFormula(formula, this.NextPattern))
            {
                satisfied = next.FindSubProperty(next.Property(p), 0).getSatisfied();
            }

            property.setSatisfied(satisfied);

            result = result || satisfied;
        }

        return result;
    }

    public void Update(Register register)
    {
        for (int i = 0; i < this.properties.size(); i++) {
            this.properties.get(i).setSatisfied(register.Property(i).getSatisfied());
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
	protected final String NotPattern ="(¬)(.*)";
	
	/*
	  Regular expressions used below to identify formula types could be combined into two general expressions
	  for uniary opertators and binary operators:

	  Uniary operator regex ([A|E|N|¬])(.*)
	  Binary operator regex ([^->|\\&\\&|\\|\\||U]*)(->|\\&\\&|\\|\\||U)(.*)

	  A bit more work is needed especially for the binary operator regex.
	*/
}
