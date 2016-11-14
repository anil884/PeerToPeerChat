package anilbehera.p2pchat.info;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anil Behera on 8/9/2015.
 * Saves information about each peer
 */
public class PeerList {

    public String ipaddress;
    public String peername;
    public List<String> registeredTopic;
    public InetAddress inetAddress;
    public String macadress;
    public Date LastUpdate;
    public boolean active=true;


    PeerList(InetAddress ip,String name,List<String> l,String macadress)
    {
        inetAddress=ip;
        ipaddress=ip.getHostAddress();
        peername=name;
        registeredTopic = new ArrayList<String>();
        registeredTopic.addAll(l);
        this.macadress=macadress;
        LastUpdate=new Date();
    }

    public static PeerList createInstance(InetAddress ip,String name,List<String> l,String macadress)
    {
        return new PeerList(ip,name,l,macadress);
    }

}
