package anilbehera.p2pchat.net;

import android.content.Context;

import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/9/2015.
 * Sends Broadcast a regular interval of time
 */
public class BroadcastManger implements Runnable{

    private Context c;
    public static boolean stopFlag=true;
    BroadcastManger(Context c)
    {
        this.c=c;
        stopFlag=true;
    }
    @Override
    public void run() {
        try {
            while (stopFlag) {
                if(RegistryInfo.macaddress==null || RegistryInfo.chatname == null)
                {
                    continue;
                }
                new Thread(new BroadcastSender(c)).start();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
