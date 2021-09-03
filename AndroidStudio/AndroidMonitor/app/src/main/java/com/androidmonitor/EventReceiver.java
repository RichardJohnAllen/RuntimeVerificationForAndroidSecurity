/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The EventReceiver class receives trace events as intents from the
AndroidInterceptor Xposed module.

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

package com.androidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.androidmonitor.reverserosuhavelund.*;

public class EventReceiver extends BroadcastReceiver {

    private TextView log;
    private ArrayList<Monitor> monitors;

    EventReceiver(TextView log, ArrayList<Monitor> monitors)
    {
        this.log = log;
        this.monitors = monitors;
    }

    public void onReceive(Context context, Intent intent) {

        TraceEvent event = new TraceEvent(new Date(intent.getLongExtra("Time", -1)),
                intent.getStringExtra("Event"),
                intent.getIntExtra("Pid", -1),
                intent.getStringExtra("AppName"),
                intent.getStringExtra("Action"));

        this.Log("Received intent: ");
        this.LogTraceEvent(event);

        Trace.Singleton().AddEvent(event);

        this.Log("Trace: " + Trace.Singleton().TraceString());

        for (int i = 0; i < this.monitors.size(); i++) {
            Monitor monitor = this.monitors.get(i);

            EvaluationResult result = monitor.Evaluate(event);

            this.Log("Event evaluated");

            if (result.Satisfied()) {

                this.LogNewLine();
                this.Log("Monitor: " + i + " " + result.Message());
                this.LogNewLine();
                this.Log("Events involved: ");
                for (TraceEvent traceEvent: result.SatisfyingEvents()) {
                    this.LogTraceEvent(traceEvent);
                }

                monitor.Reset();
            }
        }

        this.LogNewLine();
    }

    private void LogTraceEvent(TraceEvent event)
    {
        log.append(event.toString() + "\r\n");
    }

    private void Log(String message)
    {
        Date now = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String timeString = formatter.format(now);

        log.append(timeString + " " + message + "\r\n");
    }

    private void LogNewLine()
    {
        log.append("\r\n");
    }
}

