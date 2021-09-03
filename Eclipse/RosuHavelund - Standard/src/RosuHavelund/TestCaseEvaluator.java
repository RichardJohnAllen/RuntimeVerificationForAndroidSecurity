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

import java.util.Random;

public class TestCaseEvaluator
{
	static public String[] GenerateTestTraces(String[] language, int maxWordLength, int traceLength)
	{
		String[] result = new String[traceLength];
		
		for (int i = 0; i < traceLength; i++)
		{
			for (int n = 1; n <= maxWordLength; n++)
			{
				String word = GenerateWord(language, n);
				result[i] = word;
			}
		}
		
		return result;
	}
/*	
	static public boolean UntilFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;
		boolean rightOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			if (event.equals("a") && satisfied)
			{
				leftOperandSatisfied = true;
    		}
			
			if (event.equals("b"))
			{
				rightOperandSatisfied = true;
    		}
			
			satisfied = (rightOperandSatisfied || leftOperandSatisfied);
		}
		
		return satisfied;
	}
*/
/*
	static public boolean AndFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;
		boolean rightOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
    		rightOperandSatisfied = event.equals("b");
		}

		satisfied = (rightOperandSatisfied && leftOperandSatisfied);

		return satisfied;
	}
*/
/*	
	static public boolean OrFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;
		boolean rightOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
    		rightOperandSatisfied = event.equals("b");
		}

		satisfied = (rightOperandSatisfied || leftOperandSatisfied);

		return satisfied;
	}
*/
/*	
	static public boolean ImpliesFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;
		boolean rightOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
			rightOperandSatisfied = event.equals("b");
			
			satisfied = !leftOperandSatisfied || rightOperandSatisfied;
		}

		return satisfied;
	}
*/	
/*	
	static public boolean NotFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
			
			satisfied = !leftOperandSatisfied;
		}

		return satisfied;
	}
*/	
/*
	static public boolean AlwaysFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = true;

		boolean leftOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
			
			satisfied = (leftOperandSatisfied && satisfied);
		}

		return satisfied;
	}
*/
/*	
	static public boolean EventuallyFormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean leftOperandSatisfied = false;

		String[] events = trace.split(",");
		for (int i = 0; i < events.length; i++)
		{
			String event = events[i];
			
			leftOperandSatisfied = event.equals("a");
			
			satisfied = (leftOperandSatisfied || satisfied);
		}

		return satisfied;
	}
*/	
	static private Random random = new Random();
	
	static private String GenerateWord(String[] language, int wordLength)
	{
		String result = "";

		for (int n = 0; n < wordLength; n++)
		{
			int r = random.nextInt(language.length);
			
			if (result != "")
			{
				result = result + ",";
			}
			
			String character = language[r];
			result = result + character;
		}
		
		return result;
	}
}
