package anilbehera.p2pchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import anilbehera.p2pchat.info.MessageInfo;
import anilbehera.p2pchat.info.MessageList;
import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/8/2015.
 * Defines Chat room List page
 * Create chat rooms / register chat rooms
 */
public class TopicListActivity extends Activity{

    private ListView topicList;
    //public static enum topics {Technology,College,Random,Funny};
    public String topics[]= {"Technology","College","Random","Funny"};
    public ArrayAdapter<String> myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.topiclist);


        for(String s: topics)
        {
            if(!RegistryInfo.TopicList.contains(s))
                RegistryInfo.TopicList.addTopic(s);
        }
        myAdapter=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                RegistryInfo.TopicList.getTopicList());

        topicList= (ListView) findViewById(R.id.topiclist);
        topicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent,
                                    View v,
                                    int position,
                                    long id) {
                String topic=((TextView)v).getText().toString();
                StartMessageDB(topic);
                goToChatPage(topic);

            }

        });
        topicList.setAdapter(myAdapter);

    }

    /**
     * start message db
     * @param s
     */
    public void StartMessageDB(String s)
    {
        if(!RegistryInfo.RegisteredTopicList.contains(s))
        {
            RegistryInfo.RegisteredTopicList.add(s);
            Toast.makeText(getApplicationContext(),"Registered To the ChatRoom "+s,Toast.LENGTH_SHORT).show();
        }
        if(!RegistryInfo.TopicMessageInfo.containsKey(s)) {
                MessageList l = MessageList.createInstance(s);
                RegistryInfo.TopicMessageInfo.put(s, l);
        }

    }

    /**
     * Transition Activity Page
     * @param topic
     */
    public void goToChatPage(String topic)
    {
        Intent i = getIntent();
        Intent intent = new Intent(TopicListActivity.this, ChatActivity.class);
        intent.putExtra("topic_name", topic);
        intent.putExtra("name", i.getStringExtra("name"));
        startActivity(intent);
        finish();
    }

    public void createTopic(View v)
    {
        EditText chat = (EditText) this.findViewById(R.id.tp_name);
        String name=chat.getText().toString();
        chat.setText("");
        if(name.length()<=0)
            return;

        if (!RegistryInfo.TopicList.contains(name)) {
            Toast.makeText(getApplicationContext(), "ChatRoom Created", Toast.LENGTH_SHORT).show();
            RegistryInfo.TopicList.addTopic(name);
            StartMessageDB(name);
            goToChatPage(name);

        } else {
            myAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "ChatRoom Already Exist", Toast.LENGTH_SHORT).show();
            //Log.v("TopicActivity", "chat room already exist");
        }
    }

    public void refresh(View v)
    {
        Toast.makeText(getApplicationContext(), "Refreshing ChatRoom List", Toast.LENGTH_SHORT).show();
        myAdapter.notifyDataSetChanged();
    }

}

