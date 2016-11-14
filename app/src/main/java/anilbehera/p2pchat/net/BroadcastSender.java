package anilbehera.p2pchat.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/9/2015.
 *
 * Broadcasts a message using UDP datagram socket
 *
 */
public class BroadcastSender implements Runnable {




    private Context context;
    private  MulticastSocket mSocket;
    private static final String TAG = "BroadcastSender";
    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    BroadcastSender(Context context)
    {
        this.context=context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",
                Context.MODE_PRIVATE);
    }
    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        try {
            mSocket = new MulticastSocket(RegistryInfo.UDP_PORT);
            mSocket.setTimeToLive(8);
            BroadcastMessage();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void BroadcastMessage() throws IOException{

        String Message= RegistryInfo.macaddress+"@@@"+RegistryInfo.chatname+"@@@"+P2PServerService.getVoterMacadress()+"@@@";
        for(String s: RegistryInfo.RegisteredTopicList)
            Message+=(s+"@@@");

        byte messageData[] = Message.getBytes();
        int size = messageData.length;
        byte sizeData[] = new byte[4];
        int temp_int = size;
        for (int i = 0; i < sizeData.length; i++) {
            sizeData[i] = Integer.valueOf(temp_int & 0xff).byteValue();
            temp_int = temp_int >> 8;
        }

        byte msgData[] = new byte[sizeData.length + messageData.length];
        System.arraycopy(sizeData, 0, msgData, 0, sizeData.length);
        System.arraycopy(messageData, 0, msgData, sizeData.length, messageData.length);

        InetAddress address = InetAddress.getByName("224.0.0.1");
        DatagramPacket dataPacket = new DatagramPacket(msgData, msgData.length, address,
                RegistryInfo.UDP_PORT);
        mSocket.send(dataPacket);
        //Log.v(TAG,"Snt Package");

    }


}
