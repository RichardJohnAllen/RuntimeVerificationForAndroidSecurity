/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

The ReverseRosuHavelundMain class is a commandline interface for excercising
the algorithm.

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

import java.util.*;

import reverserosuhavelund.dynamicconditions.*;
import reverserosuhavelund.expressions.*;

public class ReverseRosuHavelundMain {

    public static void main(String args[])
    {
        System.out.println("Reverse Rosu-Havelund Implementation, Copyright (C) 2021 Richard John Allen");
        System.out.println("");
        System.out.println("Reverse Rosu Havelund Implementation comes with ABSOLUTELY NO WARRANTY.");
    	System.out.println("This is free software, and you are welcome to redistribute it under");
    	System.out.println("the conditions of the GNU General Public License 2.0");
        System.out.println("================================================================================");
        System.out.println("");

        String formulaString = GetFormulaString();
        String[] testTraces = GetTestTraces();
        
        System.out.println("Formula:");
        System.out.println(formulaString);
        
        Expression expressionTree = ExpressionFactory.Instance().Tree(formulaString, null);
        ArrayList<Expression> subFormulae = expressionTree.FlattenBreadthFirst();

        System.out.println("");
        System.out.println("Subformulae:");

        for (Expression subFormula : subFormulae)
        {
        	System.out.println(subFormula.Formula());
        }
        
        for (String testTrace : testTraces)
        {
	        System.out.println("");
	        System.out.println("Running Trace: <" + testTrace + ">");

//	        Monitor monitor = new Monitor(formulaString, "Collusion Detected", new CollusionConditions());
            Monitor monitor = new Monitor(formulaString, "", new DefaultConditions());

        	EvaluationResult result = null;

	        String[] events = testTrace.split(",");
	        for (int i = 0; i < events.length; i++) {
	        	
	        	TraceEvent e = new TraceEvent(Calendar.getInstance().getTime(),
	        			events[i],
	        			0,
	        			"",
	        			"");
	        	
	        	result = monitor.Evaluate(e);
	        	
	        	System.out.println("  Prefix: " + Integer.toString(i + 1) +
		        		" Event: " + e.Event() +
		        		" Formula satisfied: " + Boolean.toString(result.Satisfied()));
/*
	        	if (result.Satisfied()) {
        			System.out.println(result.Message());
        			System.out.println("Satisfying Events:");
	        		result.SatisfyingEvents().forEach((event) -> System.out.println(event.toString()));

	        		System.out.println("Resetting monitor");
	        		monitor.Reset();
	        	}
	        	else {
	        		System.out.println("Not Satisfied");
	        	}
*/
	        }
        	System.out.println("");
        	System.out.println("Actual Result = " + Boolean.toString(result.Satisfied()));
	    }	
    }

    public static String GetFormulaString()
    {
        System.out.println("Enter LTL formula:");

        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        
        if (result.isEmpty())
        {
// Example formula
        	result = "(<->((p S q) => ([-](q => (P(r))))))";
        	
// Test formula
//        	result = "(P(b))"; // PREVIOUS b
//        	result = "(!a)";  // NOT a
//        	result = "([-](a))"; // HAS ALWAYS BEEN a
//        	result = "(<->(a))"; // ONCE a
//        	result = "(a => (<->(b)))"; // a IMPLIES ONCE b
//        	result = "(a && (<->(b)))"; // a AND ONCE b        	
//        	result = "(a || b)"; // a OR b
//        	result = "(a S b)"; // a SINCE b
        	
// Final collusion formula
//        	result = "(<->(p && (<->(r && (<->(s && (<->(q))))))))";
        }
        
        return result;
    }
    
    public static String[] GetTestTraces()
    {
        System.out.println("Enter trace:");
        
        Scanner scanner = new Scanner(System.in);
        
        String traceString = scanner.nextLine();
        
        String[] result;
        
        if (traceString.isEmpty())
        {
	        result = DefaultTestTraces();
        }
        else if (traceString.equals("random"))
        {
        	result = TestCaseEvaluator.GenerateTestTraces(new String[] {"a", "b", "c", "d"}, 10, 20);
        }
        else
        {
        	result = new String[]{ traceString };
        }

        return result;
    }
    
    public static String[] DefaultTestTraces()
    {
// Example/collusion trace    	
    	return new String[] { "", "q,s,r,p", "p,r,s,q" };

/*

// Test traces
    	return new String[] { "", "a", "b", "c", "a,a", "a,b", "b,a", "b,b" };

        return new String[]
        {
        		"",       
        		"a",
        		"b",      
        		"c",     
        		
        		"a,a",
        		"a,b",   
        		"a,c",   
        		
        		"b,a",
        		"b,b",
        		"b,c",    
        		
        		"c,a",
        		"c,b",
        		"c,c",
        		
        		"a,a,a", 
        		"a,a,b", 
        		"a,a,c", 

        		"a,b,a", 
        		"a,b,b", 
        		"a,b,c", 

        		"a,c,a", 
        		"a,c,b", 
        		"a,c,c", 

        		"b,a,a", 
        		"b,a,b", 
        		"b,a,c", 

        		"b,b,a", 
        		"b,b,b", 
        		"b,b,c", 
        		
        		"b,c,a", 
        		"b,c,b", 
        		"b,c,c", 

        		"c,a,a", 
        		"c,a,b", 
        		"c,a,c", 

        		"c,b,a",
        		"c,b,b",   
        		"c,b,c",

        		"c,c,a",
        		"c,c,b",   
        		"c,c,c",
        		
        		"a,a,a,a", 
        		"a,a,a,b", 
        		"a,a,a,c", 

        		"a,a,b,a", 
        		"a,a,b,b", 
        		"a,a,b,c", 
        		
        		"a,a,c,a", 
        		"a,a,c,b", 
        		"a,a,c,c", 
        		
        		"a,b,a,a", 
        		"a,b,a,b", 
        		"a,b,a,c", 
        		
        		"a,b,b,a", 
        		"a,b,b,b",
        		"a,b,b,c", 	        		
        		
        		"a,b,c,a", 
        		"a,b,c,b", 
        		"a,b,c,c", 

        		"a,c,a,a", 
        		"a,c,a,b", 
        		"a,c,a,c", 

        		"a,c,b,a", 
        		"a,c,b,b", 
        		"a,c,b,c", 

        		"a,c,c,a", 
        		"a,c,c,b", 
        		"a,c,c,c", 

        		"b,a,a,a", 
        		"b,a,a,b", 
        		"b,a,a,c", 

        		"b,a,b,a", 
        		"b,a,b,b", 
        		"b,a,b,c", 

        		"b,a,c,a", 
        		"b,a,c,b", 
        		"b,a,c,c", 

        		"b,b,a,a", 
        		"b,b,a,b", 
        		"b,b,a,c", 

        		"b,b,b,a", 
        		"b,b,b,b", 
        		"b,b,b,c", 

        		"b,b,c,a", 
        		"b,b,c,b", 
        		"b,b,c,c", 

        		"b,c,a,a", 
        		"b,c,a,b", 
        		"b,c,a,c", 

        		"b,c,b,a", 
        		"b,c,b,b", 
        		"b,c,b,c", 

        		"b,c,c,a", 
        		"b,c,c,b", 
        		"b,c,c,c", 

        		"c,a,a,a", 
        		"c,a,a,b", 
        		"c,a,a,c", 

        		"c,a,b,a", 
        		"c,a,b,b", 
        		"c,a,b,c", 

        		"c,a,c,a", 
        		"c,a,c,b", 
        		"c,a,c,c", 

        		"c,b,a,a",
        		"c,b,a,b",
        		"c,b,a,c",

        		"c,b,b,a",   
        		"c,b,b,b",   
        		"c,b,b,c",   

        		"c,b,c,a",
        		"c,b,c,b",
        		"c,b,c,c",

        		"c,c,a,a",
        		"c,c,a,b",
        		"c,c,a,c",

        		"c,c,b,a",   
        		"c,c,b,b",   
        		"c,c,b,c",   

        		"c,c,c,a",   
        		"c,c,c,b",   
        		"c,c,c,c"

        };
*/        
    }
}