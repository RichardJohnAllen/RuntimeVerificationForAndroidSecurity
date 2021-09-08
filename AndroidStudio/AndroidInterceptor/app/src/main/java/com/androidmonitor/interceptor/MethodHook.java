/*
===============================================================================

Android Monitor Interceptor.
Intercepts operating system calls used in collusion attack to steal contacts list.

The MethodHook class is the ancestor of all hook classes and sends traces events
to the monitor process.

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


package com.androidmonitor.interceptor;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.util.Date;

import de.robv.android.xposed.XC_MethodHook;

public class MethodHook extends XC_MethodHook {

    protected void BroadcastEvent(Date time, String event) {

        this.BroadcastEvent(time, event, "");
    }

    protected void BroadcastEvent(Date time, String event, String action) {

        Context context = AndroidAppHelper.currentApplication().getBaseContext();

        Intent intent = new Intent("com.androidmonitor.TraceEvent");

        intent.putExtra("Time", time.getTime());
        intent.putExtra("Event", event);
        intent.putExtra("Pid", Process.myPid());
        intent.putExtra("AppName", this.getProcessNameByPid(context, Process.myPid()));
        if (!action.isEmpty()) {
            intent.putExtra("Action", action);
        }

        context.sendBroadcast(intent);

        String message = "broadcast event: " + intent.getStringExtra("Event") +
                " Time: " + intent.getLongExtra("Time", -1) +
                " Pid: " + intent.getIntExtra("Pid", -1) +
                " AppName: " + intent.getStringExtra("AppName");
        if (!action.isEmpty()) {
            message += " Action: " + intent.getStringExtra("Action");
        }

        LogWriter.LogMessage(message);
    }

    private String getProcessNameByPid(Context context, int pid) {

        String result = "";

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null)
        {
            for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
            {
                if(processInfo.pid == pid)
                {
                    result = processInfo.processName;
                }
            }
        }

        return result;
    }
}
