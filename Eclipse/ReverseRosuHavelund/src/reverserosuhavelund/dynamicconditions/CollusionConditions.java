/*
===============================================================================

Reverse Rosu-Havelund algorithm.
Interprets and evaluates LTL formulae over a finite trace.

The CollusionConditions class evaluates whether the metadata of trace events
involved in a suspected collusion attack is consistent with collusion.

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

package reverserosuhavelund.dynamicconditions;

import java.util.ArrayList;

import reverserosuhavelund.TraceEvent;

public class CollusionConditions extends DefaultConditions {

	@Override
	public ArrayList<TraceEvent> Met(ArrayList<TraceEvent> satisfyingEvents) {

		ArrayList<TraceEvent> result = new ArrayList<TraceEvent>();
	
		ArrayList<TraceEvent> queries = FilterEvents("q", satisfyingEvents);
		ArrayList<TraceEvent> sends = FilterEvents("s", satisfyingEvents);
		ArrayList<TraceEvent> receives = FilterEvents("r", satisfyingEvents);
		ArrayList<TraceEvent> publishes = FilterEvents("p", satisfyingEvents);
			
		publishes.forEach((p) -> {
			TraceEvent r = FindEventByPid(receives, p.Pid());
			if (r != null) {
				TraceEvent s = FindEventByAction(sends, r.Action());

				if (s != null ) {
		    		TraceEvent q = FindEventByPid(queries, s.Pid());
		    		
		    		if (q != null) {
		    			result.add(q);
		    			result.add(s);
		    			result.add(r);
		    			result.add(p);
		    		}
				}
			}
		});

		return result;
	}
	
	private ArrayList<TraceEvent> FilterEvents(String eventType, ArrayList<TraceEvent> events) {
		
		ArrayList<TraceEvent> result = new ArrayList<TraceEvent>();
		
		for (TraceEvent e : events) {
		  if (e.Event().equals(eventType)) { 
			  result.add(e);
		  }
		}
		
		return result;
	}
	
	private TraceEvent FindEventByPid(ArrayList<TraceEvent> events, int pid) {
    	
    	TraceEvent result = null;
    	int i = 0;
    	
    	while (result == null && i < events.size()) {
    		TraceEvent event = events.get(i); 
    		result = event.Pid() == pid ? event : null;
			
			i++;
    	}
    	
    	return result;
    }
    
    private TraceEvent FindEventByAction(ArrayList<TraceEvent> events, String action) {
    	
    	TraceEvent result = null;
    	int i = 0;
    	
    	while (result == null && i < events.size()) {
    		TraceEvent event = events.get(i); 
    		result = event.Action().equals(action) ? event : null;
			
			i++;
    	}
    	
    	return result;
    }
}
