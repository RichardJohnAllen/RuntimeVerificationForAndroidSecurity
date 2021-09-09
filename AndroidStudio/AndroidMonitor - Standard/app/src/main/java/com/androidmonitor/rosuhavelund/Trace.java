// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

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
