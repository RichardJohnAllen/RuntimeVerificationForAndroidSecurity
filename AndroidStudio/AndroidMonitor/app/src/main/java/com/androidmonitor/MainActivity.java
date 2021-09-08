/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The MainActivity class is the entry point for the AndroidMonitor application.

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidmonitor.reverserosuhavelund.*;
import com.androidmonitor.reverserosuhavelund.dynamicconditions.*;

public class MainActivity extends AppCompatActivity {

    private TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String collusionFormula = "(<->(p && (<->(r && (<->(s && (<->(q)))))))";

        log = (TextView) findViewById(R.id.eventTrace);
        log.setMovementMethod(new ScrollingMovementMethod());
        registerForContextMenu(log);

        this.Log("Monitoring app activity for collusion");
        this.Log(" Formula: " + collusionFormula);
        this.LogNewLine();

        ArrayList<Monitor> monitors = new ArrayList<Monitor>();
        for (int i = 0; i < 1; i++) {
            monitors.add(new Monitor(collusionFormula, "Possible collusion detected!", new CollusionConditions()));
        }

        getApplicationContext().registerReceiver(
                new EventReceiver(log, monitors),
                new IntentFilter("com.androidmonitor.TraceEvent"));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.setHeaderTitle("");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.clearLog){
            log.setText("");
        }
        else {
            return false;
        }

        return true;
    }

    private void Log(String message)
    {
        Date now = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String timeString = formatter.format(now);

        log.append(timeString + " " + message + "\r\n");
    }

    private void LogNewLine()
    {
        log.append("\r\n");
    }
}