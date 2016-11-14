package anilbehera.p2pchat.net;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import anilbehera.p2pchat.info.PeerList;
import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/10/2015.
 *
 * Removes inactive peers and their informations
 * vote and elect leaders
 */
public class P2PServerService extends Thread {
    private static boolean amILeader = true;
    private static String LeaderMacAdress;
    private static String LeaderVote=RegistryInfo.macaddress;
    private static HashMap<String,String> voteCount= new HashMap<String,String>();

    public static boolean flag=true;

    P2PServerService()
    {
        flag=true;
    }

    public synchronized static String getVoterMacadress()
    {
        return LeaderVote;
    }
    public synchronized static void updateVote(String user,String vote)
    {
        voteCount.put(user,vote);
    }


    @Override
    public void run()
    {
        while(flag)
        {
            if(RegistryInfo.macaddress==null || RegistryInfo.chatname == null)
            {
                continue;
            }
            try {

                UpdatePeerTopicInfo();
                UpdateLeaderVote();

                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static synchronized void UpdateLeaderVote()
    {
        HashMap<String,Integer> count=new HashMap<String, Integer>();
        int highestcount=0;
        //Log.i("LeaderChoose:","Old Leader"+LeaderMacAdress);
        LeaderMacAdress=RegistryInfo.macaddress;
        for(String s : voteCount.keySet())
        {
            String vote=voteCount.get(s);
            int c=0;
            if(count.containsKey(vote))
            {
                c=count.get(vote);
            }
            if(++c > highestcount) {
                LeaderMacAdress = vote;
                highestcount=c;
            }
            count.put(vote,c);
        }
        voteCount.clear();
        //Log.i("LeaderChoose:", "New Leader" + LeaderMacAdress);
        //ReVoting for new leader
        if(!RegistryInfo.peers.isEmpty()) {
            PeerList leader;
            int c = 0;
            do {
                leader = RegistryInfo.peers.get(new Random().nextInt(RegistryInfo.peers.size()));
            }
            while (!leader.active && c++ < 10);
            if (leader.active) {
                LeaderVote = leader.macadress;
            } else {
                LeaderVote = RegistryInfo.macaddress;
            }
        }
        else
            LeaderVote = RegistryInfo.macaddress;

        //Log.i("LeaderChoose:","New Vote By me"+LeaderVote);

    }

    private void UpdatePeerTopicInfo()
    {
        long now=new Date().getTime();
        for(PeerList peer :  RegistryInfo.peers)
        {
            if((now-peer.LastUpdate.getTime()) > 15000 && peer.active){
                //Log.i("PeerService","Peer Removing"+ peer.peername);
                for(String s: peer.registeredTopic)
                {
                    RegistryInfo.TopicUserInfo.get(s).removepeer(peer);
                    if(RegistryInfo.TopicUserInfo.get(s).isEmptyPeerList() &&
                            !"Technology".equalsIgnoreCase(s) &&
                            !"College".equalsIgnoreCase(s) &&
                            !"Random".equalsIgnoreCase(s) &&
                            !"Funny".equalsIgnoreCase(s))
                        RegistryInfo.TopicList.removeTopic(s);


                }
                peer.active=false;
            }
        }

    }

}
