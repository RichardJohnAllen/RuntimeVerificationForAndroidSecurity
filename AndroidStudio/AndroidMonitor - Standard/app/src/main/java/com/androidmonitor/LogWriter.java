// Copyright Richard John Allen 29/4/2020

package com.androidmonitor;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

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

        LogWriter.LogMessage("Received Time: " + time.toString() +
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