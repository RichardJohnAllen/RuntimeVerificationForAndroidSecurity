/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The ExpressionFactory class constructs a syntax tree of expressions when
given a formula.  Expression classes are registered by searching for .class
files containing expressions.

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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ExpressionFactory {

	public static ExpressionFactory Instance() {
	
		if (instance == null) {
			
			instance = new ExpressionFactory();
		}
		
		return instance;
	}
	
	public Expression Tree(String formula, Expression previousTree) {
		
		Expression result = null;
		
		formula = StripOuterBraces(formula);
		
		if (!formula.isEmpty()) {

			Constructor<? extends Expression> constructor;
			try {
				
				Class<? extends Expression> expressionClass = getExpressionClass(formula);
				
				if (expressionClass != null) {
					constructor = expressionClass.getConstructor(String.class, expressionClass);
					result = constructor.newInstance(formula, previousTree);
				}

			} catch (NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

				e.printStackTrace();
			}
		}
		
		return result;
	}

	private static ExpressionFactory instance;

	private ArrayList<Class<? extends Expression>> registeredExpressions;

	private ExpressionFactory() {
		
		this.registeredExpressions = new ArrayList<Class<? extends Expression>>();
		this.RegisterExpressions();
	}

	private void RegisterExpressions() {

		this.registeredExpressions.add(AlwaysBeenExpression.class);
		this.registeredExpressions.add(AndExpression.class);
		this.registeredExpressions.add(OnceExpression.class);
		this.registeredExpressions.add(ImpliesExpression.class);
		this.registeredExpressions.add(LiteralExpression.class);
		this.registeredExpressions.add(PreviousExpression.class);
		this.registeredExpressions.add(NotExpression.class);
		this.registeredExpressions.add(OrExpression.class);
		this.registeredExpressions.add(SinceExpression.class);
	}

	private Class<? extends Expression> getExpressionClass(String formula) {
		
		Class<? extends Expression> result = null;
		
		int i = 0;
		while (result == null && i < registeredExpressions.size()) {
		
			try {

				Class<? extends Expression> expressionClass = registeredExpressions.get(i);
				
				Method matchMethod = expressionClass.getMethod("Match", String.class);
				Boolean matches = (Boolean)(matchMethod.invoke(null, formula));
				
				result = matches? expressionClass : null;

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {

				e.printStackTrace();
			}

			i++;
		}
		
		return result;
	}
	
    private String StripOuterBraces(String formula)
    {
    	String result = formula.trim();

    	if (result.startsWith("("))
    	{
    		result = result.substring(1);
    	}
    	
    	if (result.endsWith(")"))
		{
    		result = result.substring(0, result.length() - 1);
		}
    	
    	return result;
    }
}