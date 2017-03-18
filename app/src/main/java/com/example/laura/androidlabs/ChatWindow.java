package com.example.laura.androidlabs;

import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class ChatWindow extends AppCompatActivity {
    protected ListView listView;
    protected EditText chatEditText;
    protected Button btSend;
    private ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected static ChatDatabaseHelper chatDataHelper;
    protected SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatDataHelper = new ChatDatabaseHelper(this);
        db = chatDataHelper.getWritableDatabase();

        Cursor c = db.query(false, chatDataHelper.TABLE_NAME, new String[] {chatDataHelper.KEY_ID, chatDataHelper.KEY_MESSAGE}, chatDataHelper.KEY_ID +" not null", null, null, null, null, null, null);
        int rows = c.getCount();
        c.moveToFirst();
        while(! c.isAfterLast()){
            chatMessage.add(c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString( c.getColumnIndex( chatDataHelper.KEY_MESSAGE)));
            c.moveToNext();
        }

        listView = (ListView) findViewById(R.id.listView);
        chatEditText = (EditText) findViewById(R.id.editChat);
        btSend = (Button) findViewById(R.id.buttonSend);

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(chatEditText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

                ContentValues newValues = new ContentValues();
                newValues.put(chatDataHelper.KEY_MESSAGE, chatEditText.getText().toString());
                db.insert(chatDataHelper.TABLE_NAME, "", newValues );
                chatEditText.setText("");
            }
        });

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
        for(int i=0; i<c.getColumnCount(); i++){
            System.out.print(c.getColumnName(i));
    }

    }

     @Override
     protected void onDestroy(){
        super.onDestroy();
        chatDataHelper.close();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

    public class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessage.size();
        }

        public String getItem(int position) {
            return chatMessage.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

    }

    }



