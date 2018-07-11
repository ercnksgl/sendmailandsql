package com.example.bsk.mail;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.konusarakogren.email.emailproject.R;

import java.util.ArrayList;
import java.util.List;

import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView addMail;
    RecyclerAdapter recyclerAdapter;
    List<String> to_list = new ArrayList<>();
    List<String> from_list = new ArrayList<>();
    List<String> subject_list = new ArrayList<>();
    List<String> content = new ArrayList<>();

    List<String> to_list_db = new ArrayList<>();
    List<String> from_list_db = new ArrayList<>();
    List<String> subject_list_db = new ArrayList<>();
    List<String> content_db = new ArrayList<>();


    private Dialog dialog;
    private MailgunEmail mailgunEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.activity_main_recycler);
        addMail = findViewById(R.id.fab);

        readFromDB();
        addMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                // Creating email model to send


                final EditText fromText = dialog.findViewById(R.id.from);
                final EditText toText = dialog.findViewById(R.id.to);
                final EditText subjectText = dialog.findViewById(R.id.subject);
                final EditText contentText = dialog.findViewById(R.id.content);

                dialog.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Creating email model to send
                        mailgunEmail = new MailgunEmail.Builder()
                                .addTo(new MailgunEmail.Contact(toText.getText().toString()))
                                .from(new MailgunEmail.Contact(fromText.getText().toString()))
                                .subject(subjectText.getText().toString())
                                .text(contentText.getText().toString())
                                .deliveryTime(System.currentTimeMillis())
                                .build();

                        new SendEmail().execute();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }


    public class SendEmail extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            try {
                // Creating Mailgun service
                MailGunService mailGunService = new MailGunService(
                        HttpUrl.parse("https://api.mailgun.net/v3"),
                        "konusarakogren.com",
                        "key-f1620623df90057445d83c38aacf6fdc");

                boolean success = mailGunService.sendMail(mailgunEmail);

                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean resultStatus) {
            if (resultStatus) {
                Toast.makeText(MainActivity.this, "Email is Sent!", Toast.LENGTH_SHORT).show();
                saveToDB(mailgunEmail.to.toString(),
                        mailgunEmail.from.toString(),
                        mailgunEmail.subject,
                        mailgunEmail.text);
            }
        }
    }


    public void saveToDB(String to, String from, String subject,String content) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.PERSON_COLUMN_TO, to);
            cv.put(DataBaseHelper.PERSON_COLUMN_FROM, from);
            cv.put(DataBaseHelper.PERSON_COLUMN_CONTENT, content);
            cv.put(DataBaseHelper.PERSON_COLUMN_SUBJECT, subject);
            db.insert(DataBaseHelper.PERSON_TABLE_NAME, null, cv);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }


    public List<String> readFromDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        try {
            String[] stunlar = {DataBaseHelper.PERSON_COLUMN_ID,DataBaseHelper.PERSON_COLUMN_TO, DataBaseHelper.PERSON_COLUMN_FROM, DataBaseHelper.PERSON_COLUMN_CONTENT, DataBaseHelper.PERSON_COLUMN_SUBJECT};
            Cursor cursor = db.query(DataBaseHelper.PERSON_TABLE_NAME, stunlar, null, null, null, null, null);
            while (cursor.moveToNext()) {
                to_list_db.add( cursor.getString(1));
                from_list_db.add(cursor.getString(2));
                content_db.add(cursor.getString(3));
                subject_list_db.add(cursor.getString(4));


            }
            adapterPush();
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();


        return to_list_db;

    }


    public void adapterPush() {


        for(int i =0 ; i<to_list_db.size();i++){
            to_list.add(to_list_db.get(i));
            from_list.add(from_list_db.get(i));
            subject_list.add(subject_list_db.get(i));
            content.add(content_db.get(i));
        }

        recyclerAdapter = new RecyclerAdapter(to_list, from_list, subject_list, content, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }
}