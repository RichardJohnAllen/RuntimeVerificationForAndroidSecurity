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

import java.util.ArrayList;

public class Trace
{
    private ArrayList<TraceEvent> events;

    public Trace()
    {
        this.events = new ArrayList<TraceEvent>();	
    }
    
    public Trace(String[] events)
    {
        this();

        for (String e : events)
        {
            this.events.add( new TraceEvent(e));
        }
    }

    public void AddEvent(String event)
    {
    	this.events.add( new TraceEvent(event));
    }
    
    public TraceEvent Event(int i)
    {
        return this.events.get(i);
    }

    public int Length()
    {
        return this.events.size();
    }
    
    public String toString()
    {
    	ArrayList<String> result = new ArrayList<String>();
    	
    	for (TraceEvent event : this.events)
    	{
    		result.add(event.Value());
    	}
    	
    	return result.toString();
    }
}
