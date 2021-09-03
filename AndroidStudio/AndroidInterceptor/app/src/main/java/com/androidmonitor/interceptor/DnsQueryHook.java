// Copyright Richard John Allen 4/5/2020

package com.androidmonitor.interceptor;

import java.util.Calendar;

import de.robv.android.xposed.XC_MethodHook;

public class DnsQueryHook extends MethodHook {

    @Override
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

        LogWriter.LogDnsQuery(param);

        this.BroadcastEvent(Calendar.getInstance().getTime(), "q");

        super.beforeHookedMethod(param);
    }
}
