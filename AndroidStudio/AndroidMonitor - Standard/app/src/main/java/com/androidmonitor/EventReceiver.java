// Copyright Richard John Allen 29/4/2020

package com.androidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import com.androidmonitor.rosuhavelund.EvaluationResult;
import com.androidmonitor.rosuhavelund.Monitor;
import com.androidmonitor.rosuhavelund.Trace;
import com.androidmonitor.rosuhavelund.TraceEvent;

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

        this.log.append("Received intent, " + event.toString() + "\r\n");

        Trace.Singleton().AddEvent(event);

        this.log.append("Trace: " + Trace.Singleton().TraceString() + "\r\n");

        for (Monitor monitor : this.monitors) {
            EvaluationResult result = monitor.Evaluate(Trace.Singleton());
            if (result.Satisfied()) {
                this.log.append("\r\n");
                this.log.append(result.Message() + "\r\n");
                this.log.append("\r\n");
                this.log.append("Events involved: \r\n");
                for (TraceEvent traceEvent: result.SatisfyingEvents()) {
                    this.log.append(traceEvent.toString() + "\r\n");
                }
            }
        }

        this.log.append("\r\n");
    }
}

