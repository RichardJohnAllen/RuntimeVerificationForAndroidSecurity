// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

import java.util.ArrayList;

public class DefaultRegister extends Register
{
    public DefaultRegister(ArrayList<SyntaxTree> nodes)
    {
        super();

        this.properties = new ArrayList<Property>();

        for (SyntaxTree node : nodes)
        {
            this.properties.add(new Property(node));
        }

        for (int p = this.properties.size() - 1; p >= 0; p--)
        {
            Property property = this.properties.get(p);
            String formula = property.Formula();
            
            if (this.EvaluateLiteralFormula(formula))
            {
                property.setSatisfied(false);
            }
            else if (this.EvaluateUnaryFormula(formula, this.AlwaysPattern))
            {
                property.setSatisfied(true);
            }
            else if (this.EvaluateUnaryFormula(formula, this.EventuallyPattern))
            {
                Property subProperty = this.FindSubProperty(property, 0);

                property.setSatisfied(subProperty.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.ImpliesPattern))
            {
                Property antecendent = this.FindSubProperty(property, 0);
                Property conclusion = this.FindSubProperty(property, 1);

                property.setSatisfied(!antecendent.getSatisfied() || conclusion.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.AndPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                property.setSatisfied(leftProposition.getSatisfied() && rightProposition.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.UntilPattern))
            {
               Property post = this.FindSubProperty(property, 1);

                property.setSatisfied(post.getSatisfied());
            }
            else if (this.EvaluateUnaryFormula(formula, this.NotPattern))
            {
                Property subProperty = this.FindSubProperty(property, 0);

                property.setSatisfied(!subProperty.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.OrPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                property.setSatisfied(leftProposition.getSatisfied() || rightProposition.getSatisfied());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.XorPattern))
            {
                Property leftProposition = this.FindSubProperty(property, 0);
                Property rightProposition = this.FindSubProperty(property, 1);

                property.setSatisfied(leftProposition.getSatisfied() ^ rightProposition.getSatisfied());
            }
            else if (this.EvaluateUnaryFormula(formula, this.NextPattern))
            {
                property.setSatisfied(false);
            }
        }
    }
}
