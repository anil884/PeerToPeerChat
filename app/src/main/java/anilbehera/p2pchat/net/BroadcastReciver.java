package anilbehera.p2pchat.net;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import anilbehera.p2pchat.info.PeerList;
import anilbehera.p2pchat.info.RegistryInfo;
import anilbehera.p2pchat.info.TopicPeerInfo;

/**
 * Created by Anil Behera on 8/9/2015.
 * Maintains UDP socket to receive broadcated information from other peers
 */
public class BroadcastReciver implements Runnable{

    public static boolean stopFlag=true;
    private Context context;
    private MulticastSocket mSocket;
    private InetAddress receiveAddress;
    private static final String TAG = "BroadcastReciver";
    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    BroadcastReciver(Context context)
    {
        stopFlag=true;
        this.context=context;
        try {
            this.mSocket = new MulticastSocket(RegistryInfo.UDP_PORT);
            this.receiveAddress = InetAddress.getByName("224.0.0.1");
            this.mSocket.joinGroup(receiveAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        byte buffer[] = new byte[2048];
        DatagramPacket dataPacket = new DatagramPacket(buffer, 2048);

        while(stopFlag)
        {
            if(RegistryInfo.macaddress==null || RegistryInfo.chatname == null)
            {
                continue;
            }
            try{
                mSocket.receive(dataPacket);
                InetAddress address = dataPacket.getAddress();
                String ip_add=address.getHostAddress();


                int size = ((((buffer[3] & 0xff) << 24) | ((buffer[2] & 0xff) << 16)
                        | ((buffer[1] & 0xff) << 8) | ((buffer[0] & 0xff) << 0)));

                byte messageDate[] = new byte[size];
                System.arraycopy(buffer, 4, messageDate, 0, messageDate.length);
                String Message[]=parse(messageDate);
                String peerMacAdress = Message[0];
                String peername=Message[1];
                String vote=Message[2];

                if(peerMacAdress != null && !peerMacAdress.equalsIgnoreCase(RegistryInfo.macaddress) && !peerMacAdress.equalsIgnoreCase("null") )
                {

                    Log.v(TAG,"Meessage:  "+ new String(messageDate,"UTF-8"));
                    List<String> topics=new ArrayList<String>();
                    for(int i=3;i<Message.length;i++)
                        topics.add(Message[i]);

                    PeerList peer=updatepeerlist(address,peername,topics,peerMacAdress);
                    updateTopicList(peer);
                    //updating peers vote
                    P2PServerService.updateVote(peerMacAdress,vote);

                }
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void updateTopicList(PeerList peer) {
        //Log.v(TAG, "Coming to TopicList");

            for(String name : peer.registeredTopic) {
                if (!RegistryInfo.TopicList.contains(name)) {
                    RegistryInfo.TopicList.addTopic(name);
                }
            }

        for(String s :  peer.registeredTopic)
        {
            //Log.v(TAG, "adding for Topic :"+s);
            if(RegistryInfo.TopicUserInfo.containsKey(s))
            {
                RegistryInfo.TopicUserInfo.get(s).addpeerList(peer);
            }
            else
            {
                TopicPeerInfo tu_info= TopicPeerInfo.createInstance(s,peer);
                RegistryInfo.TopicUserInfo.put(s,tu_info);
            }
        }
    }

    public PeerList updatepeerlist(InetAddress address,String peername, List<String> topics,String macadress) {
        for(PeerList peer :  RegistryInfo.peers)
            if(macadress.equalsIgnoreCase(peer.macadress))
            {
                peer.peername=peername;
                peer.registeredTopic.clear();
                peer.registeredTopic.addAll(topics);
                peer.inetAddress =address;
                peer.LastUpdate=new Date();
                peer.active=true;
                return peer;
            }
        PeerList peer=PeerList.createInstance(address,peername,topics,macadress);
        RegistryInfo.peers.add(peer);
        return peer;
    }

    private String [] parse(byte[] messageDate) {
        String str = "";
        try {
            str = new String(messageDate,"UTF-8");

           // Log.v(TAG,"BroadCast Message Received"+ str);
           // Log.v(TAG,"---------------------------------------------");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.split("@@@");
    }

}
