package com.example.laura.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {
    String usermessage;
    Long messageId;

    ChatWindow chatWindow = null;

    public MessageFragment(){

    }

    public MessageFragment(ChatWindow cw){
        chatWindow = cw;
    }


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Bundle bun = getArguments();
        usermessage = bun.getString("Message");
        messageId = bun.getLong("ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View gui = inflater.inflate(R.layout.fragment_layout, null);
        TextView message = (TextView) gui.findViewById(R.id.messageHere);
        message.setText(usermessage);
        TextView id = (TextView)gui.findViewById(R.id.messageID);
        id.setText("ID:" + messageId );

        Button btnDelete = (Button) gui.findViewById(R.id.deleteMessage);

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.i("MessageFragment", "User clicked Delete Message button");
                if (chatWindow == null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DeleteID", messageId);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
                else
                {
                    chatWindow.deleteListMessage(messageId);
                    chatWindow.removeFragment();
                }
            }

        });

        return gui;
    }
}
