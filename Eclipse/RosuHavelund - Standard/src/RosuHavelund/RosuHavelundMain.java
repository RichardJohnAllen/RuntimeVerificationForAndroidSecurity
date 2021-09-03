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

import java.util.*;

public class RosuHavelundMain {

    public static void main(String args[])
    {
        System.out.println("Rosu-Havelund Implementation, Copyright (C) 2021 Richard John Allen");
        System.out.println("Rosu-Havelund Implementation comes with ABSOLUTELY NO WARRANTY.");
    	System.out.println("This is free software, and you are welcome to redistribute it under");
    	System.out.println("the conditions of the GNU General Public License 2.0");
        System.out.println("================================================================================");
        System.out.println("");

        System.out.println("Enter LTL formula:");

        Scanner scanner = new Scanner(System.in);
        String formulaString = scanner.nextLine();
        
        if (formulaString.isBlank())
        {
			formulaString = "(A((p U q) -> (E(q -> (N(r))))))";
        	
//        	formulaString = "((!b) U a)";
//        	formulaString = "(a U b)";
//        	formulaString = "(a -> (E(b)))";
//        	formulaString = "(a && (E(b)))";
//        	formulaString = "(A(a))";
//        	formulaString = "(!a)";
//        	formulaString = "(a && (E(b)))";
//        	formulaString = "(N(b))";
        }

        System.out.println("Enter trace:");
        
        String traceString = scanner.nextLine();
        
        String[] testTraces;
        if (traceString.isBlank())
        {
        	testTraces = new String[] { "", "q,s,r,p", "p,r,s,q" };
//        	testTraces = new String[] { "", "a", "b", "a,a", "a,b", "b,a", "b,b" };
/*			
			  testTraces = new String[] { "", "a", "b", "c",
			  
			  "a,a", "a,b", "a,c",
			  
			  "b,a", "b,b", "b,c",
			  
			  "c,a", "c,b", "c,c",
			  
			  "a,a,a", "a,a,b", "a,a,c",
			  
			  "a,b,a", "a,b,b", "a,b,c",
			  
			  "a,c,a", "a,c,b", "a,c,c",
			  
			  "b,a,a", "b,a,b", "b,a,c",
			  
			  "b,b,a", "b,b,b", "b,b,c",
			  
			  "b,c,a", "b,c,b", "b,c,c",
			  
			  "c,a,a", "c,a,b", "c,a,c",
			  
			  "c,b,a", "c,b,b", "c,b,c",
			  
			  "c,c,a", "c,c,b", "c,c,c",
			  
			  "a,a,a,a", "a,a,a,b", "a,a,a,c",
			  
			  "a,a,b,a", "a,a,b,b", "a,a,b,c",
			  
			  "a,a,c,a", "a,a,c,b", "a,a,c,c",
			  
			  "a,b,a,a", "a,b,a,b", "a,b,a,c",
			  
			  "a,b,b,a", "a,b,b,b", "a,b,b,c",
			  
			  "a,b,c,a", "a,b,c,b", "a,b,c,c",
			  
			  "a,c,a,a", "a,c,a,b", "a,c,a,c",
			  
			  "a,c,b,a", "a,c,b,b", "a,c,b,c",
			  
			  "a,c,c,a", "a,c,c,b", "a,c,c,c",
			  
			  "b,a,a,a", "b,a,a,b", "b,a,a,c",
			  
			  "b,a,b,a", "b,a,b,b", "b,a,b,c",
			  
			  "b,a,c,a", "b,a,c,b", "b,a,c,c",
			  
			  "b,b,a,a", "b,b,a,b", "b,b,a,c",
			  
			  "b,b,b,a", "b,b,b,b", "b,b,b,c",
			  
			  "b,b,c,a", "b,b,c,b", "b,b,c,c",
			  
			  "b,c,a,a", "b,c,a,b", "b,c,a,c",
			  
			  "b,c,b,a", "b,c,b,b", "b,c,b,c",
			  
			  "b,c,c,a", "b,c,c,b", "b,c,c,c",
			  
			  "c,a,a,a", "c,a,a,b", "c,a,a,c",
			  
			  "c,a,b,a", "c,a,b,b", "c,a,b,c",
			  
			  "c,a,c,a", "c,a,c,b", "c,a,c,c",
			  
			  "c,b,a,a", "c,b,a,b", "c,b,a,c",
			  
			  "c,b,b,a", "c,b,b,b", "c,b,b,c",
			  
			  "c,b,c,a", "c,b,c,b", "c,b,c,c",
			  
			  "c,c,a,a", "c,c,a,b", "c,c,a,c",
			  
			  "c,c,b,a", "c,c,b,b", "c,c,b,c",
			  
			  "c,c,c,a", "c,c,c,b", "c,c,c,c" };
*/			 
        }
        else if (traceString.equals("random"))
        {
        	testTraces = TestCaseEvaluator.GenerateTestTraces(new String[] {"a", "b", "c", "d"}, 10, 20);
        }
        else
        {
        	testTraces = new String[]{ traceString };
        }

        scanner.close();

        System.out.println("Formula:");
        System.out.println(formulaString);
        
        SyntaxTree syntaxTree = new SyntaxTree(formulaString);
        ArrayList<SyntaxTree> subFormulae = syntaxTree.Flatten();

        System.out.println("");
        System.out.println("Subformulae:");

        for (SyntaxTree subFormula : subFormulae)
        {
        	System.out.println(subFormula.Formula());
        }
        
        for (String testTrace : testTraces)
        {
	        System.out.println("");
	        System.out.println("Running Trace: [" + testTrace + "]");
	
	        Monitor monitor = new Monitor(subFormulae);
	        
	        String[] events = testTrace.split(",");
	        Trace trace = new Trace();

        	boolean satisfied = false;

        	for (String event : events)
	        {
	        	trace.AddEvent(event);

	        	System.out.println("");
	        	System.out.println("  Prefix: " + trace.toString());

	        	satisfied = monitor.Evaluate(trace);
		
		        System.out.println("  Formula satisfied = " + Boolean.toString(satisfied));
	        }
	        
        	System.out.println("");
	        System.out.println("  Actual Result = " + Boolean.toString(satisfied));
        	System.out.println("");
	    }
    }
}