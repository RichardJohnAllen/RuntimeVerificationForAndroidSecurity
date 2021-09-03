/*
===============================================================================

Android Monitor Interceptor.
Intercepts operating system calls used in collusion attack to steal contacts list.

The MethodHooks class is notified when a package is loaded hooks the methods we are
interested in if that package contains them.

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

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DnsResolver;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ListAdapter;

import androidx.annotation.RequiresApi;

import java.net.InetAddress;
import java.util.concurrent.Executor;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class MethodHooks implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        LogWriter.LogMessage("handleLoadPackage: " + lpparam.packageName);

        this.hookContactsQuery(lpparam.classLoader);
        this.hookIntents(lpparam.classLoader);
        this.hookListViewDisplay(lpparam.classLoader);
  //      this.hookDnsResolver(lpparam.classLoader);
  //      this.hookDnsQuery(lpparam.classLoader);
    }

    private void hookContactsQuery(ClassLoader classLoader) {

        LogWriter.LogMethodHook("android.content.ContentResolver", "query");
        findAndHookMethod("android.content.ContentResolver",
                classLoader,
                "query",
                Uri.class,
                String[].class,
                String.class,
                String[].class,
                String.class,
                new ContentResolverQueryHook());
    }

    private void hookIntents(ClassLoader classLoader) {

        LogWriter.LogMethodHook("android.content.ContextWrapper", "sendBroadcast");
        findAndHookMethod("android.content.ContextWrapper",
                classLoader,
                "sendBroadcast",
                Intent.class,
                new ContextWrapperSendBroadcastHook());

        LogWriter.LogMethodHook("android.content.ContextWrapper", "registerReceiver");
        findAndHookMethod("android.content.ContextWrapper",
                classLoader,
                "registerReceiver",
                BroadcastReceiver.class,
                IntentFilter.class,
                new ContextWrapperRegisterReceiverHook(classLoader));
    }

    private void hookListViewDisplay(ClassLoader classLoader)
    {

        LogWriter.LogMethodHook("android.widget.ListView", "setAdapter");
        findAndHookMethod("android.widget.ListView",
                classLoader,
                "setAdapter",
                ListAdapter.class,
                new SetAdapterHook());
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void hookDnsResolver(ClassLoader classLoader) {

        LogWriter.LogMethodHook("android.net.DnsResolver", "query");
        findAndHookMethod("android.net.DnsResolver",
                classLoader,
                "query",
                Network.class,
                String.class,
                int.class,
                Executor.class,
                CancellationSignal.class,
                DnsResolver.Callback.class,
                new DnsResolverHook());
    }
*/
/*
    private void hookDnsQuery(ClassLoader classLoader) {

        LogWriter.LogMethodHook("java.net.InetAddress", "getByName");
        findAndHookMethod("java.net.InetAddress",
                classLoader,
                "getByName",
                String.class,
                new DnsQueryHook());
    }
 */
}