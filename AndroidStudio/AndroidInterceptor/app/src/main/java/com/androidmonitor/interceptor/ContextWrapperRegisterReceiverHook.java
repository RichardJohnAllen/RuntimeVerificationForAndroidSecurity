/*
===============================================================================

Android Monitor Interceptor.
Intercepts operating system calls used in collusion attack to steal contacts list.

The ContextWrapperRegisterReceiverHook class hooks O/S calls to register an intent
receiver and then hooks the receipt of intents.

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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class ContextWrapperRegisterReceiverHook extends XC_MethodHook
{
    public ContextWrapperRegisterReceiverHook(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
        BroadcastReceiver receiver = (BroadcastReceiver) param.args[0];
        IntentFilter filter = (IntentFilter) param.args[1];

        if (receiver != null && filter != null)
        {
            LogWriter.LogRegisterReceiverCall(param);

            if (classLoader != null)
            {
                String receiverClassName = receiver.toString();
                int end = receiverClassName.indexOf("@");
                receiverClassName = receiverClassName.substring(0, end);

                if (receiverClassName != "com.androidmonitor.EventReceiver") // Don't hook our monitors broadcast receiver
                {
                    try
                    {
                        LogWriter.LogMethodHook(receiverClassName, "onReceive");

                        findAndHookMethod(receiverClassName,
                                classLoader,
                                "onReceive",
                                Context.class,
                                Intent.class,
                                new BroadcastReceiverHook());
                    }
                    catch (XposedHelpers.ClassNotFoundError e)
                    {
                        LogWriter.LogMessage("Android Monitor class not found " + receiverClassName);
                    }
                    catch (NoSuchMethodError e)
                    {
                        LogWriter.LogMessage("Android Monitor method not found " + receiverClassName + "." + "onReceive");
                    }
                }
            }
        }
        super.beforeHookedMethod(param);
    }

    private ClassLoader classLoader;
}