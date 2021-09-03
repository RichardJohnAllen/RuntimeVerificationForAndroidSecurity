/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The TraceEvent class stores the information about a trace event.

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

package com.androidmonitor.reverserosuhavelund;

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

