/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

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

package com.androidmonitor.reverserosuhavelund.dynamicconditions;

import java.util.ArrayList;
import java.util.Date;

import com.androidmonitor.reverserosuhavelund.TraceEvent;

public class CollusionConditions extends DynamicConditions {

	@Override
	public ArrayList<TraceEvent> Met(ArrayList<TraceEvent> satisfyingEvents) {

		ArrayList<TraceEvent> result = new ArrayList<TraceEvent>();
	
		ArrayList<TraceEvent> queries = FilterEvents("q", satisfyingEvents);
		ArrayList<TraceEvent> sends = FilterEvents("s", satisfyingEvents);
		ArrayList<TraceEvent> receives = FilterEvents("r", satisfyingEvents);
		ArrayList<TraceEvent> publishes = FilterEvents("p", satisfyingEvents);

		for (TraceEvent p : publishes) {
			TraceEvent r = FindEarlierEventByPid(receives, p.Pid(), p.Time());
			if (r != null) {
				TraceEvent s = FindEarlierEventByAction(sends, r.Action(), r.Time());

				if (s != null ) {
					TraceEvent q = FindEarlierEventByPid(queries, s.Pid(), s.Time());

					if (q != null) {
						result.add(q);
						result.add(s);
						result.add(r);
						result.add(p);
					}
				}
			}
		}

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
	
	private TraceEvent FindEarlierEventByPid(ArrayList<TraceEvent> events, int pid, Date time) {
    	
    	TraceEvent result = null;
    	int i = 0;
    	
    	while (result == null && i < events.size()) {
    		TraceEvent event = events.get(i); 
    		result = (event.Pid() == pid && event.Time().before(time)) ? event : null;

    		i++;
    	}
    	
    	return result;
    }
    
    private TraceEvent FindEarlierEventByAction(ArrayList<TraceEvent> events, String action, Date time) {
    	
    	TraceEvent result = null;
    	int i = 0;
    	
    	while (result == null && i < events.size()) {
    		TraceEvent event = events.get(i); 
    		result = (event.Action().equals(action) && event.Time().before(time)) ? event : null;

			i++;
		}
    	
    	return result;
    }
}
