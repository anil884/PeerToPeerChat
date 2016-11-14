package anilbehera.p2pchat.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Behera on 8/9/2015.
 *
 * Holds the Information of a peer user
 */
public class TopicPeerInfo {


    private String topicname;

    private List<PeerList> registeredpeers;

    TopicPeerInfo(String t,  PeerList p)
    {
        topicname=t;
        registeredpeers=new ArrayList<PeerList>();
    }

    public synchronized void updatepeerList(List<PeerList> list)
    {
        registeredpeers.clear();
        registeredpeers.addAll(list);
    }
    public synchronized void addpeerList(PeerList list)
    {
        if(!registeredpeers.contains(list))
            registeredpeers.add(list);
    }

    public synchronized List<PeerList> getRegisteredpeers()
    {
        return registeredpeers;
    }
    public synchronized void removepeer(PeerList peer)
    {
        registeredpeers.remove(peer);
    }
    public synchronized boolean isEmptyPeerList()
    {
        return registeredpeers.isEmpty();
    }

    public String getTopicname()
    {
        return topicname;
    }

    public static TopicPeerInfo createInstance(String s, PeerList p)
    {
        return new TopicPeerInfo(s,p);
    }
}
