/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

The UnaryExpression class is the root of all LTL expressions with a single
operand.  The operand is called the left operand. 

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

package reverserosuhavelund.expressions;

import java.util.ArrayList;

abstract class UnaryExpression extends Expression {

	private String operator;
	private Expression leftOperand;
	
    public String Operator() { return this.operator; }
    public Expression LeftOperand() { return this.leftOperand; }

    protected UnaryExpression(String formula, String operator, UnaryExpression previous) {
    	
    	super(formula, previous);
    	
    	this.operator = operator;
    	
    	int operatorPos = ExpressionMatcher.MatchPosition(this.Formula(), operator);
    	
    	String operand = this.Formula().substring(0, operatorPos);
    	if (operand.isEmpty()) {
    		operand = this.Formula().substring(operatorPos + this.operator.length());
    	}

    	this.leftOperand = ExpressionFactory.Instance().Tree(operand,
    			(previous != null)? previous.LeftOperand() : null);
    }
    
    @Override
    protected void Flatten(ArrayList<Expression> flat)
    {
    	flat.add(this.LeftOperand());
    }
}
