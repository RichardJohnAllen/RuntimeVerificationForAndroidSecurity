/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The BinaryExpression class is the root of all LTL expressions with two
operands.  It descends from the UnaryExpression and adds a right operand.

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

package com.androidmonitor.reverserosuhavelund.expressions;

import java.util.ArrayList;

abstract class BinaryExpression extends UnaryExpression {
	
	private Expression rightOperand;

	protected BinaryExpression(String formula, String operator, BinaryExpression previous) {

		super(formula, operator, previous);
		
		int operatorPos = ExpressionMatcher.MatchPosition(this.Formula(), this.Operator());
    	
    	String operand = this.Formula().substring(operatorPos + this.Operator().length());

 		this.rightOperand = ExpressionFactory.Instance().Tree(operand,
   				(previous != null)? previous.RightOperand() : null);    		
	}
	
    public Expression RightOperand() { return this.rightOperand; }
    
    @Override
    protected void Flatten(ArrayList<Expression> flat) {
    	
    	super.Flatten(flat);
    	flat.add(this.RightOperand());
    }
}
