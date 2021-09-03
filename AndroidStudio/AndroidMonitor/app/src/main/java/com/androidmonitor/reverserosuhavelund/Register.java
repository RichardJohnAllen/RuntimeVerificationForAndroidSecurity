/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The Register class is a collection of subexpressions of a formula.

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

package com.androidmonitor.reverserosuhavelund;

import java.util.ArrayList;

import com.androidmonitor.reverserosuhavelund.expressions.Expression;

public class Register {

	private ArrayList<Expression> expressions;
	
	public Register(ArrayList<Expression> expressions)
	{
		this.expressions = expressions;
	}
	
	public Boolean Evaluate(String event) {
		
		Boolean anyExpressionSatisfied = false;
		
		for (int i = this.expressions.size() - 1; i >= 0; i--) {

			Expression expression = this.expressions.get(i);
			Boolean satisfied = expression.Evaluate(event);
			
			anyExpressionSatisfied = anyExpressionSatisfied || satisfied;
		}
		
		return anyExpressionSatisfied;
	}

	public boolean Satisfied() {
		
		return this.expressions.get(0).Satisfied();
	}

	public void Update(Register register) {
		
		for (int i = this.expressions.size() - 1; i >= 0 ; i--) {
			
			Expression expression = this.expressions.get(i); 
			expression.setSatisfied(register.getExpression(i).Satisfied());
		}
	}
	
	public void Reset() {
		for (int i = this.expressions.size() - 1; i >= 0 ; i--) {
			
			this.expressions.get(i).Reset(); 
		}
	}

	private Expression getExpression(int i) {
		
		return this.expressions.get(i);
	}
}