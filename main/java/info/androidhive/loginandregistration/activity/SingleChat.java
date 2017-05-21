package info.androidhive.loginandregistration.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.androidhive.loginandregistration.R;



    import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SingleChat extends AppCompatActivity {
        private static final String TAG = "ChatActivity";

        private ChatArrayAdapter chatArrayAdapter;
        private ListView listView;
        private EditText chatText;
        private Button buttonSend;
        private boolean side = false;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_single_chat);

            buttonSend = (Button) findViewById(R.id.send);

            listView = (ListView) findViewById(R.id.msgview);

            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
            listView.setAdapter(chatArrayAdapter);

            chatText = (EditText) findViewById(R.id.msg);
            chatText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        return sendChatMessage();
                    }
                    return false;
                }
            });
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    sendChatMessage();
                }
            });



            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver, new IntentFilter("newmessage"));



            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            listView.setAdapter(chatArrayAdapter);

            //to scroll the list view to bottom on data change
            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            });
        }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            String message=intent.getStringExtra("message");
            Log.e("broadlog","inside onreceive in the activity " + message);
            Toast.makeText(getApplicationContext(),
                    "The Received Message is:"+message, Toast.LENGTH_LONG).show();

            //  ... react to local broadcast message
        }
    };


        private boolean sendChatMessage() {
            chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
            chatText.setText("");
            side = !side;
            return true;
        }
    }
