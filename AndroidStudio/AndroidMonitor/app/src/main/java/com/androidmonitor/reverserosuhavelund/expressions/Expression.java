/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The Expression class is the root of the LTL expression hierarchy.

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

import java.util.ArrayList;

public abstract class Expression {

	private String formula;
	private Expression previous;
	private boolean satisfied;
	
	protected Expression(String formula, Expression previous) {
    	
    	this.formula = formula;
    	this.previous = previous;
    }
    
    public String Formula() { return this.formula; }
    public Expression Previous() { return this.previous; }
    
    public void setSatisfied(boolean satisfied) { this.satisfied = satisfied; }
    public boolean Satisfied() { return this.satisfied; }

    public abstract boolean Evaluate(String event);
    
    public ArrayList<Expression> FlattenBreadthFirst()
    {
        ArrayList<Expression> flat = new ArrayList<Expression>();
        
        flat.add(this);

    	int i = 0;
    	while (i < flat.size())
    	{
        	Expression expression = flat.get(i);
        	expression.Flatten(flat);
            i++;
    	}

        return flat;
    }
    
    public abstract void Reset();

    protected void Flatten(ArrayList<Expression> flat) { };   
}