/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The LogWriter class write log message to the Xposed log.

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

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

public class LogWriter
{
    public static void LogReceivedTraceEventIntent(Intent intent)
    {
        Date time = new Date(intent.getLongExtra("Time", -1 ));
        String event = intent.getStringExtra("Event");
        int pid = intent.getIntExtra("Pid", 0);
        String appName = intent.getStringExtra("AppName");
        String processName = "";//getProcessNameByPid(pid);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String timeString = formatter.format(time);

        LogWriter.LogMessage("Received Time: " + timeString +
                " Event: " + event +
                " Pid: " + pid +
                " AppName: " + appName +
                " Process Name: " + processName);
    }

    public static void LogMessage(String message)
    {
        XposedBridge.log(message);
    }

    private static String getProcessNameByPid(int pid)
    {
        String result = "";

        Application application = AndroidAppHelper.currentApplication();
        if (application != null)
        {
            Context context = application.getApplicationContext();
            if (context != null)
            {
                ActivityManager manager
                        = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
            }
        }

        return result;
    }
}