// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TraceEvent
{
    private Date time;
    private String event;
    private int pid;
    private String appName;
    private String action;

    public TraceEvent(Date time, String event, int Pid, String appName, String action) {
        this.time = time;
        this.event = event;
        this.pid = Pid;
        this.appName = appName;
        this.action = action;
    }

    public Date Time() { return this.time; }

    public String Event()
    {
        return this.event;
    }

    public int Pid()
    {
        return this.pid;
    }

    public String AppName() { return this.appName; }

    public String Action() { return this.action; }

    public String toString() {
        String result =
                " Time: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(this.time) + "\r\n" +
                " Event: " + this.event + "\r\n" +
                " Pid: " + this.pid + "\r\n" +
                " AppName: " + this.appName + "\r\n";

        if (this.action != null) {
            result += " Action: " + this.action + "\r\n";
        }

        return result;
    }
}
