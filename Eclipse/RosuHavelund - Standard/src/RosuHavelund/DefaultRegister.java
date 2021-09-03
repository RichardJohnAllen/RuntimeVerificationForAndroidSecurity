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
                property.setValid(false);
            }
            else if (this.EvaluateUnaryFormula(formula, this.AlwaysPattern))
            {
                property.setValid(true);
            }
            else if (this.EvaluateUnaryFormula(formula, this.EventuallyPattern))
            {
                Property subProperty = this.SubProperty(property, 0);

                property.setValid(subProperty.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.ImpliesPattern))
            {
                Property antecendent = this.SubProperty(property, 0);
                Property conclusion = this.SubProperty(property, 1);

                property.setValid(!antecendent.getValid() || conclusion.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.AndPattern))
            {
                Property leftProposition = this.SubProperty(property, 0);
                Property rightProposition = this.SubProperty(property, 1);

                property.setValid(leftProposition.getValid() && rightProposition.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.UntilPattern))
            {
               Property post = this.SubProperty(property, 1);

               property.setValid(post.getValid());
            }
            else if (this.EvaluateUnaryFormula(formula, this.NotPattern))
            {
               Property subProperty = this.SubProperty(property, 0);

                property.setValid(!subProperty.getValid());
            }
            else if (this.EvaluateBinaryOperatorFormula(formula, this.OrPattern))
            {
                Property leftProposition = this.SubProperty(property, 0);
                Property rightProposition = this.SubProperty(property, 1);

                property.setValid(leftProposition.getValid() || rightProposition.getValid());
            }
            else if (this.EvaluateUnaryFormula(formula, this.NextPattern))
            {
                property.setValid(false);
            }
        }
    }
}
