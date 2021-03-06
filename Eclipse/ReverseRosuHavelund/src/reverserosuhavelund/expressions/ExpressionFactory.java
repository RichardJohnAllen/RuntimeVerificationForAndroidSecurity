/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

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

package reverserosuhavelund.expressions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
		
		try {
			this.registeredExpressions = new ArrayList<Class<? extends Expression>>();
			this.RegisterExpressions();
			
		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();
		}
	}

	private void RegisterExpressions()
        throws ClassNotFoundException, IOException {
        
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = "ReverseRosuHavelund/Expressions";
	    Enumeration<URL> resources = classLoader.getResources(path);
	    
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    
	    for (File directory : dirs) {
	    	FindExpressionClasses(directory, "reverserosuhavelund.expressions");
	    }
	    
	}
	
	private void FindExpressionClasses(File directory, String packageName) {

		if (directory.exists()) {
	    	
		    File[] files = directory.listFiles();
		    for (File file : files) {
		    	
		        if (file.isDirectory()) {
		        	
		            assert !file.getName().contains(".");
		            FindExpressionClasses(file, packageName + "." + file.getName());
		        } else if (file.getName().endsWith(".class")) {

		        	try {
		        		Class<?> cls = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
		        		
		        		if (Expression.class.isAssignableFrom(cls) 
		        				&& (cls.getMethod("Match", String.class) != null)) {
							
		        			Class<? extends Expression> exp = (Class<? extends Expression>) cls;
		        			this.registeredExpressions.add(exp);
						}
					} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) { };
		        }
		    }
	    }
	}
	
	private Class<? extends Expression> getExpressionClass(String formula) {
		
		Class<? extends Expression> result = null;
		
		int i = 0;
		while (result == null && i < registeredExpressions.size()) {
		
			try {

				Class<? extends Expression> expressionClass = registeredExpressions.get(i);
				
				Method match = expressionClass.getMethod("Match", String.class);
				Boolean matches = (Boolean)(match.invoke(null, formula));
				
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
    	String result = formula.strip();

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