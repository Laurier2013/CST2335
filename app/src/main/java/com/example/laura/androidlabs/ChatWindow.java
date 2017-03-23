package com.example.laura.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class ChatWindow extends AppCompatActivity {
    protected ListView listView;
    protected EditText chatEditText;
    protected Button btSend;
    private ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected static ChatDatabaseHelper chatDataHelper;
    protected SQLiteDatabase db;
    protected Boolean isTablet;
    Cursor results;
    ChatAdapter messageAdapter;
    MessageFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        isTablet = (findViewById(R.id.fragmentHolder)!=null); // boolean variable to tell if it's a tablet

        chatDataHelper = new ChatDatabaseHelper(this);
        db = chatDataHelper.getWritableDatabase();

        results = db.query(false, chatDataHelper.TABLE_NAME, new String[] {chatDataHelper.KEY_ID, chatDataHelper.KEY_MESSAGE}, chatDataHelper.KEY_ID +" not null", null, null, null, null, null, null);
        int rows = results.getCount();
        results.moveToFirst();
        while(! results.isAfterLast()){
            chatMessage.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( chatDataHelper.KEY_MESSAGE)));
            results.moveToNext();
        }

        listView = (ListView) findViewById(R.id.listView);
        chatEditText = (EditText) findViewById(R.id.editChat);
        btSend = (Button) findViewById(R.id.buttonSend);

        messageAdapter = new ChatAdapter(this);
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
                results = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                        new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE }, null, null, null, null, null, null);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("chatListView", "onItemClick: " + i + " " + l);

                Bundle bundle = new Bundle();
                bundle.putLong("ID", l);//l is the database ID of selected item
                String message = messageAdapter.getItem(i);
                bundle.putString("Message", message);

                //step 2, if a tablet, insert fragment into FrameLayout, pass data
                if(isTablet) {
                    frag = new MessageFragment(ChatWindow.this);

                    frag.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, frag).commit();
                }
                //step 3 if a phone, transition to empty Activity that has FrameLayout
                else //isPhone
                {
                    Intent intnt = new Intent(ChatWindow.this, MessageDetails.class);
                    intnt.putExtra("ID" , l);
                    intnt.putExtra("Message", message);//pass the Database ID and message to next activity
                    startActivityForResult(intnt, 5); //go to view fragment details
                }
            }
        });

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + results.getColumnCount());
        for(int i=0; i<results.getColumnCount(); i++){
            System.out.print(results.getColumnName(i));

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

        public long getItemId(int position) {
            results.moveToPosition(position);
            return results.getLong( results.getColumnIndex(ChatDatabaseHelper.KEY_ID) );
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
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if ((requestCode == 5)&&(resultCode == Activity.RESULT_OK))
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        Long deleteId = data.getLongExtra("DeleteID", -1);
        deleteListMessage(deleteId);
    }

    public void deleteListMessage(Long id)
    {
        final SQLiteDatabase db = chatDataHelper.getWritableDatabase();

        db.delete(ChatDatabaseHelper.TABLE_NAME, "_id = " + id , null);
        chatMessage.clear();
        results = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},null, null, null, null, null, null);
        int rows = results.getCount();
        results.moveToFirst();
        while(!results.isAfterLast()) {
            chatMessage.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            results.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

    public void removeFragment()
    {
        getSupportFragmentManager().beginTransaction().remove(frag).commit();
    }
    }



