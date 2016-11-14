package anilbehera.p2pchat.net;

import android.app.Service;
import anilbehera.p2pchat.net.BroadcastReciver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Anil Behera on 8/9/2015.
 * This is the main code for background services
 * Here, we intiate all threads for background service
 */
public class PeerDiscoveryService extends Service{


    private Thread reciver;
    private Thread sender;
    private static String TAG="DiscoverService";
    private TCPReciverServer tcpReciverServer;
    private P2PServerService maintainceServer;
    //private IBinder binder = new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Log.v(TAG, "IN PeerDiscoveryService");


        SharedPreferences sharedPreferences = getSharedPreferences("user",
                Context.MODE_PRIVATE);

        sender= new Thread(new BroadcastManger(this));
        reciver = new Thread(new BroadcastReciver(this));
        tcpReciverServer=new TCPReciverServer();
        maintainceServer=new P2PServerService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.v(TAG, "Starting the service");
        sender.start();
        reciver.start();
        tcpReciverServer.start();
        maintainceServer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
       // Log.v(TAG, "something fucked up");
       // Log.v(TAG, "PeerDiscoveryService destroyed");


        //serviceHandler=null;
        sender.stop();
        reciver.stop();
        tcpReciverServer.stop();
        maintainceServer.stop();
        //BroadcastManger.finish();
        //BroadcastReciver.finish();
        super.onDestroy();
    }


}
