package com.example.sqliteexample.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqliteexample.R;
import com.example.sqliteexample.data.DBHandler;
import com.example.sqliteexample.model.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    TextView textView;
    EditText editText;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.my_text_view);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        DBHandler db = new DBHandler(this);

        //db.clearDB(contacts);

        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));


        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");

        refreshDB();
    }

    public void enterClick(View view) {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addContact(new Contact(editText.getText().toString(), editText2.getText().toString()));

        refreshDB();

    }

    public void deleteClick(View view) {
        DBHandler dbHandler = new DBHandler(this);

        dbHandler.deleteContact(new Contact(editText.getText().toString(), editText2.getText().toString()));

        refreshDB();
    }



    protected void refreshDB() {
        DBHandler dbHandler = new DBHandler(this);
        List<Contact> contacts = dbHandler.getAllContacts();

        StringBuilder sBuilder = new StringBuilder();

        for (Contact contact : contacts) {
            String log = "Id: " + contact.get_id() +
                    " ,Name: " + contact.getName() +
                    " ,Phone: " + contact.getPhone() + "\n";

            sBuilder.append(log);

        }
        textView.setText(sBuilder);
    }

    public void seeInfo(View view) {
        DBHandler dbHandler = new DBHandler(this);
        List<Contact> contacts = dbHandler.getAllContacts();

        TextView tV = (TextView) view;
        Toast.makeText(MainActivity.this, tV.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}