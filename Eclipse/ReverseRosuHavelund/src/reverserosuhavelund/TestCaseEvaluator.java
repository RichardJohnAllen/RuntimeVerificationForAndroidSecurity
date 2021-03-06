/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

The TestCaseEvaluator class is for automated testing

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

package reverserosuhavelund;

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
	static public boolean FormulaSatisfied(String formula, String trace)
	{
		boolean satisfied = false;

		boolean aFound = false;
		boolean bFollowing = false;
		boolean cFollowing = false;

		for (int i = 0; i < trace.length(); i++)
		{
			if (trace.charAt(i) == 'a')
			{
				aFound = true;
			}
			
			if (aFound && trace.charAt(i) == 'b')
			{
				bFollowing = true;
			}
			
			if (bFollowing && trace.charAt(i) == 'c')
			{
				cFollowing = true;
			}
		}
		
		satisfied = aFound && bFollowing && cFollowing;
		
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
