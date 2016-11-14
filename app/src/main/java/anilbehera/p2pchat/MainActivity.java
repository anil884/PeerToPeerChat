package anilbehera.p2pchat;
/**
 * Created by Anil Behera on 8/9/2015.
 * Defines the start page
 * Intiate backgroud services
 */
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import anilbehera.p2pchat.info.MessageList;
import anilbehera.p2pchat.info.PeerList;
import anilbehera.p2pchat.info.RegistryInfo;
import anilbehera.p2pchat.info.TopicListInfo;
import anilbehera.p2pchat.info.TopicPeerInfo;
import anilbehera.p2pchat.net.BroadcastManger;
import anilbehera.p2pchat.net.BroadcastReciver;
import anilbehera.p2pchat.net.PeerDiscoveryService;

/**
 * Class for Login purpose and start background service
 */
public class MainActivity extends Activity {

    private EditText nameEditText;
    private TextView uuidTextView;
    private Button okButton;
    private Button continueButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String TAG = "MainActivity";
    private WifiManager wm;

    public void IntiateVariables(){
        RegistryInfo.RegisteredTopicList=new ArrayList<String>();
        RegistryInfo.chatname=null;
        RegistryInfo.peers=new ArrayList<PeerList>();
        RegistryInfo.TopicUserInfo= new HashMap<String, TopicPeerInfo>();
        RegistryInfo.TopicMessageInfo=new HashMap<String,MessageList>();
        RegistryInfo.ipaddress=null;
        RegistryInfo.macaddress=null;
        RegistryInfo.TopicList=new TopicListInfo();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG,"IN MainActivity");
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Wifi Details
        wm = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifi_info=wm.getConnectionInfo();


        IntiateVariables();
        RegistryInfo.ipaddress = Formatter.formatIpAddress(wifi_info.getIpAddress());
        RegistryInfo.macaddress = wifi_info.getMacAddress();

        nameEditText = (EditText) this.findViewById(R.id.name);
        okButton = (Button) this.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(okClickListener);
    }

    /**
     * start background service
     */
    private void startServerService() {
        ActivityManager myManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(100);
        boolean flag = true;
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals("anilbehera.p2pchat.net.PeerDiscoveryService")) {

                flag = false;
                break;
            }
        }
        if (flag) {
            startService(new Intent(MainActivity.this, PeerDiscoveryService.class));
        }
        else
        {
            BroadcastReciver.stopFlag=true;
            BroadcastManger.stopFlag=true;
        }
    }



    private View.OnClickListener okClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(!wm.isWifiEnabled())
            {
                Toast.makeText(getApplicationContext(),"Enable Wi-Fi to Login",Toast.LENGTH_LONG).show();
                return;
            }
            String name = nameEditText.getText().toString();
            if( name.equalsIgnoreCase("") || name.contains("@"))
            {
                Toast.makeText(getApplicationContext(),"Provide Proper Login Name",Toast.LENGTH_SHORT).show();
                return;
            }

            okButton.setVisibility(View.GONE);
            nameEditText.setEnabled(false);

            editor.putString("name", name);
            RegistryInfo.chatname=name;

            editor.commit();

            startServerService();
            Intent intent = new Intent(MainActivity.this, TopicListActivity.class);
            startActivity(intent);
            finish();

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
