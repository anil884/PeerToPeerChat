package anilbehera.p2pchat.net;

import android.util.Log;

import java.util.List;

import anilbehera.p2pchat.info.MessageInfo;
import anilbehera.p2pchat.info.PeerList;
import anilbehera.p2pchat.info.RegistryInfo;
import anilbehera.p2pchat.info.TopicPeerInfo;

/**
 * Created by Anil Behera on 8/10/2015.
 * Implements to create multiple TCP connection and send message to every body
 */
public class TCPMultiSender {

    public static void sendmessage(MessageInfo msg)
    {
        TopicPeerInfo info=RegistryInfo.TopicUserInfo.get(msg.topicname);
        if(info==null)
            return;
        List<PeerList> peers=info.getRegisteredpeers();
        for(PeerList peer: peers)
        {
            //Log.v("MultiSender","Sending To:"+peer.peername);
            new TCPSender(peer.inetAddress,msg.toString()).start();
        }
    }
}
