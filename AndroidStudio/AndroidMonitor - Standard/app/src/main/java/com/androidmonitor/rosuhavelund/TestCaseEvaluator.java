// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

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
