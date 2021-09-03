/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

The Trace class holds and provides access to trace events.

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

import java.util.ArrayList;

public class Trace
{
    private static Trace singleton;
    private ArrayList<TraceEvent> events;

    public static Trace Singleton()
    {
        if (singleton == null)
        {
            singleton = new Trace();
        }

        return singleton;
    }

    public Trace()
    {
        this.events = new ArrayList<TraceEvent>();	
    }

    public void AddEvent(TraceEvent event)
    {
        this.events.add(event);
    }

    public TraceEvent Event(int i)
    {
        return this.events.get(i);
    }

    public int Length()
    {
        return this.events.size();
    }
    
    public String TraceString()
    {
        String result = "";

        for (int i = 0; i < this.Length(); i++) {
            TraceEvent event = this.Event(i);

            result += event.Event() + ",";
        }

        return result;
    }
}