package com.android.acid.Publisher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * This class demonstrates collusion between two applications that exchange the contacts using a
 * broadcast. This application will receive the broadcast from the ConSender application
 */
public class MainActivity extends Activity {
    //Used for testing purposes
    private static final String TAG = "MyActivity";

    //Used to receive the broadcast
    AppReceiver mAppReceiver = new AppReceiver();

    // This list contains all the contacts
    ListView contactList;

    // This is the layout for the application
    LinearLayout mainLayout;

    // To display
    EditText editLog;

    TextView receivedCountTextView;

    /**
     * This is run whn the application is first started up
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mAppReceiver, new IntentFilter("ContactStealTwo"));
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        contactList = (ListView) findViewById(R.id.ContactList);
        receivedCountTextView = (TextView) findViewById(R.id.ReceivedCount);
        editLog = (EditText) findViewById(R.id.editLog);
        editLog.setText("Awaiting contacts\r\n\r\n");

        editLog.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                editLog.setText("");
            }
        });
    }

    /**
     * This is the class created to receive the intent
     */
    public class AppReceiver extends BroadcastReceiver {

        private Integer receivedCount = 0;

        /**
         * We handle the received intent here
         */
        public void onReceive(Context context, Intent intent) {
            // List created to store the acquired information
            List<String> stolenData;

            // Places the received information into a new ArrayList which we can use
            stolenData = intent.getStringArrayListExtra("array");

            WriteLog(" Received " + stolenData.size() + " contacts");

            for (String contact : stolenData)
            {
                WriteLog(contact);
            }
            WriteLog("");

            this.receivedCount++;
            receivedCountTextView.setText(receivedCount.toString());

            //This sets an adapter to use the array and display elements as a list item
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_list_item_1,
                    stolenData );

            //Displays the populated ListView
            contactList.setAdapter(arrayAdapter);
        }
    }

    private void WriteLog(String message) {

        Date now = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String timeString = formatter.format(now);

        editLog.append(timeString + " " + message + "\r\n");
    }
}
