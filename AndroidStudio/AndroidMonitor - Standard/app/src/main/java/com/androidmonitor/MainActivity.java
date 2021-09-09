// Copyright Richard John Allen 29/4/2020

package com.androidmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.androidmonitor.rosuhavelund.Monitor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String collusionFormula = "((((E(s)) && ((¬s) U q)) && ((E(r)) && ((¬r) U s))) && ((E(p)) && ((¬p) U r)))";

        log = (TextView) findViewById(R.id.eventTrace);
        log.setMovementMethod(new ScrollingMovementMethod());
/*
        log.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                log.setText("");
            }
        });
*/
        log.append("Monitoring app activity for collusion" + "\r\n");
        log.append(" Formula: " + collusionFormula + "\r\n");
        log.append("\r\n");

        ArrayList<Monitor> monitors = new ArrayList<Monitor>();
        for (int i = 0; i < 5; i++) {
            monitors.add(new Monitor(collusionFormula, "Possible collusion detected!"));
        }

        getApplicationContext().registerReceiver(
                new EventReceiver(log, monitors),
                new IntentFilter("com.androidmonitor.TraceEvent"));
    }
}