/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The SinceExpression class implements the evaluation of the
LTL SINCE operator.

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

class SinceExpression extends BinaryExpression {
	
	public SinceExpression(String formula, SinceExpression previous) {
		
		super(formula, operator, previous);
		this.Reset();
	}

	@Override
	public boolean Evaluate(String formula) {

		this.setSatisfied(
				this.RightOperand().Satisfied() ||
				(this.LeftOperand().Satisfied() && this.Previous().Satisfied())
		);

		return this.Satisfied();
	}
	
	@Override
	public void Reset() {
		this.setSatisfied(true);
	}
	
	private static final String operator = "S";
	
	public static boolean Match(String formula)
	{
		return ExpressionMatcher.Matches(formula, operator);
	}
	
	public static int MatchPosition(String formula)
	{
		return ExpressionMatcher.MatchPosition(formula, operator);
	}
}
