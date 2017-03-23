package com.example.laura.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        MessageFragment frag = new MessageFragment(null);
        Bundle bun = getIntent().getExtras();
        frag.setArguments(bun);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, frag).commit();
    }
}
