package anilbehera.p2pchat.info;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anilbehera.p2pchat.ChatActivity;
import anilbehera.p2pchat.net.PeerDiscoveryService;

/**
 * Created by Anil Behera on 8/9/2015.
 *
 * Miantains static and constant values
 */
public class RegistryInfo {

    public static List<String> RegisteredTopicList=new ArrayList<String>();
    public static String chatname;
    public static List<PeerList> peers=new ArrayList<PeerList>();
    public static HashMap<String, TopicPeerInfo> TopicUserInfo= new HashMap<String, TopicPeerInfo>();
    public static HashMap<String,MessageList> TopicMessageInfo=new HashMap<String,MessageList>();
    final public static int UDP_PORT = 6006;
    final public static int TCP_PORT = 6007;
    final public static String BroadcastUUID = "anilbehera.p2p";
    public static String ipaddress;
    public static String macaddress;
    public static String topicname="";
    public static Handler chatpageHandler=null;
    //public static ArrayAdapter chatPage=null;
    public static TopicListInfo TopicList=new TopicListInfo();
    public static PeerDiscoveryService service;



}
