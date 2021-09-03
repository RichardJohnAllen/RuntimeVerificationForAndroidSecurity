/*
===============================================================================

Android Monitor Interceptor.
Intercepts operating system calls used in collusion attack to steal contacts list.

The ContentResolverQueryHook class hooks O/S calls to query the contacts list.

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

import android.net.Uri;
import android.provider.ContactsContract;

import java.util.Calendar;

public class ContentResolverQueryHook extends MethodHook
{
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
        LogWriter.LogQueryCall(param);

        Uri uri = (Uri)param.args[0];

        if(uri.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_URI))
        {
            this.BroadcastEvent(Calendar.getInstance().getTime(), "q");
        }
        super.beforeHookedMethod(param);
    }
}
