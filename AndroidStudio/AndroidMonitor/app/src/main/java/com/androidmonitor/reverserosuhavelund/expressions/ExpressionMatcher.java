/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The ExpressionMatcher class determines the root operator of a formula
by searching the formula for a given operator.

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

abstract class ExpressionMatcher {

	public static boolean Matches(String formula, String operator) {
		return MatchPosition(formula, operator) >= 0;
	}

	public static int MatchPosition(String formula, String operator)
	{
		int start = 0;
		int pos = formula.indexOf(operator, start);
		
    	while(pos >= 0 && OperatorInsideBraces(pos, formula)) {
    		start = pos + operator.length();
    		pos = formula.indexOf(operator, start);
    	} 

		return pos;
	}

	private static boolean OperatorInsideBraces(int pos, String formula)
	{
		int braceCount = 0;
		
		for (int i = 0; i < pos; i++)
		{
			char character = formula.charAt(i); 
			switch (character)
			{
				case '(':
					braceCount++;
					break;
				case ')':
					braceCount--;
					break;
			}
		}
		
		return (pos >= 0 && braceCount > 0);
	}
}
