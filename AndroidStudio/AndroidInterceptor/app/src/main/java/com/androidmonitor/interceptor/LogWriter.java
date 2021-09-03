/*
===============================================================================

Android Monitor Interceptor.
Intercepts operating system calls used in collusion attack to steal contacts list.

The LogWriter class writes messages to a log.

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
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Process;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class LogWriter
{
    private static String LogMethodCallMessage(XC_MethodHook.MethodHookParam param) {
        long threadId = Thread.currentThread().getId();
        int processId = Process.myPid();
        String processName = getProcessNameByPid(processId);

        return "Android Interceptor intercepted method call: " + param.method.getName() +
               ", Thread Id: " + String.valueOf(threadId) +
               ", Process Id: " + String.valueOf(processId) +
               ", Process Name: " + processName;
    }

    public static void LogRegisterReceiverCall(XC_MethodHook.MethodHookParam param) {
        BroadcastReceiver receiver = (BroadcastReceiver) param.args[0];
        IntentFilter filter = (IntentFilter) param.args[1];

        String logMessage = LogMethodCallMessage(param) +
                ", BroadcastReceiver: " + receiver.toString() +
                ", IntentFilter Action: " + filter.getAction(0);

        XposedBridge.log(logMessage);
    }

    public static void LogQueryCall(XC_MethodHook.MethodHookParam param) {
        Uri uri = (Uri)param.args[0];

        String logMessage = LogMethodCallMessage(param) + ", Uri: " + uri;

        XposedBridge.log(logMessage);
    }

    public static void LogOnBroadcastReceivedCall(XC_MethodHook.MethodHookParam param) {
        Intent intent = (Intent) param.args[1];

        String logMessage = LogMethodCallMessage(param) +
                ", Intent Action: " + intent.getAction();

        XposedBridge.log(logMessage);
    }

    public static void LogSendBroadcast(XC_MethodHook.MethodHookParam param) {
        Intent intent = (Intent)param.args[0];

        String logMessage = LogMethodCallMessage(param) +
                ", Intent Action: " + intent.getAction();

        XposedBridge.log(logMessage);
    }

    public static void LogSetAdapter(XC_MethodHook.MethodHookParam param) {
        String logMessage = LogMethodCallMessage(param);

        XposedBridge.log(logMessage);
    }

    public static void LogDnsQuery(XC_MethodHook.MethodHookParam param) {
        String logMessage = LogMethodCallMessage(param);

        XposedBridge.log(logMessage);
    }

    public static void LogMethodHook(String className, String methodName) {
        long threadId = Thread.currentThread().getId();
        int processId = Process.myPid();
        String processName = getProcessNameByPid(processId);

        XposedBridge.log("Android Interceptor hooking " + className + "." + methodName +
                ", Thread Id: " + String.valueOf(threadId) +
                ", Process Id: " + String.valueOf(processId) +
                ", Process Name: " + processName
        );
    }

    public static void LogMessage(String message) {
        XposedBridge.log("Android Interceptor " + message);
    }

    public static String getProcessNameByPid(int pid) {
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