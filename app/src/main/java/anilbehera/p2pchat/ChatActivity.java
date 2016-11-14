package anilbehera.p2pchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


import anilbehera.p2pchat.info.MessageInfo;
import anilbehera.p2pchat.info.RegistryInfo;
import anilbehera.p2pchat.net.TCPMultiSender;

/**
 * Created by Anil Behera on 8/8/2015.
 *
 * Defines chat Page of the application
 * Sends and recives chat messages for a chat room
 */
public class ChatActivity extends Activity {

    //private String chatanme;
    private String topicname;
    private ArrayAdapter myAdapter;
    private Handler ListUpdateHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat_activity);


        Intent intent=getIntent();
        //chatanme=intent.getStringExtra("name");
        topicname=intent.getStringExtra("topic_name");
        RegistryInfo.topicname=topicname;
        TextView topic_bar=(TextView)this.findViewById(R.id.topic_bar_name);
        topic_bar.setText(topicname);

        myAdapter=new ChatListAdpater(this, R.layout.chat_item,
                RegistryInfo.TopicMessageInfo.get(topicname).getmessages() );
        ListView chatbox= (ListView) findViewById(R.id.chatbox);
        chatbox.setAdapter(myAdapter);

        addHandler();

    }


    private void addHandler()
    {
        ListUpdateHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1) {
                    //Log.i("ChatActivity","Sending Message");
                    myAdapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };
        RegistryInfo.chatpageHandler=ListUpdateHandler;
    }

    public void RefreshChat(View v)
    {
        myAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Chat Refressing",Toast.LENGTH_SHORT).show();
    }

    public void goback(View view) {

        RegistryInfo.topicname="";
        RegistryInfo.chatpageHandler=null;
        Intent intent = new Intent(ChatActivity.this, TopicListActivity.class);
        intent.putExtra("name",RegistryInfo.chatname);
        startActivity(intent);
        finish();
    }

    public void SendMessage(View view)
    {
        EditText chat = (EditText) this.findViewById(R.id.sendmessage);
        String msg=chat.getText().toString();
        if(msg.length()<=0)
            return;
        chat.setText("");
        String time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        MessageInfo message=MessageInfo.createInstance(RegistryInfo.chatname,time,msg,topicname);
        RegistryInfo.TopicMessageInfo.get(topicname).addmessage(message);
        TCPMultiSender.sendmessage(message);
        //Log.v("Chat", "Coming to Send a Message:" + msg);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }



}
