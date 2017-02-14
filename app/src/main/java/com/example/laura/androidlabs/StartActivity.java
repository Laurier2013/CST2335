package com.example.laura.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = " StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btReference = (Button) findViewById(R.id.button);
        btReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent nextActivity = new Intent(StartActivity.this, ListItemsActivity.class);
               startActivityForResult(nextActivity, 5);
            }
        });

        Button btStartChat = (Button) findViewById(R.id.buttonChat);
        btStartChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent chatWindow = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(chatWindow);
                Log.i(ACTIVITY_NAME, "USer Clicked Start Chart");
            }
        });

        Log.i(ACTIVITY_NAME, "In onCreate()");
    }

    public void onResume(){
        super.onRestart();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    public void onDestroy(){
        super.onDestroy();
       Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void onActivityResult(int requestCode,int responseCode, Intent data){
        if( requestCode == 5){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }

        if(responseCode== Activity.RESULT_OK) {
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this , messagePassed, Toast.LENGTH_LONG);
            toast.show(); //display your message box
        }
    }
}
