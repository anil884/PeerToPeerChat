package anilbehera.p2pchat.info;

/**
 * Created by Anil Behera on 8/9/2015.
 * saves details of each message and their meta data information
 */
public class MessageInfo {
    public String peername="";
    public String time="";
    public String message="";
    public String topicname="";
    public String display="";

    MessageInfo(String peername,String time,String message,String topicname)
    {
        this.peername=peername;
        this.time=time;
        this.message=message;
        this.topicname=topicname;
        display=peername+"::"+message;
    }

    public static MessageInfo createInstance(String peername,String time,String message,String topicname)
    {
        return new MessageInfo(peername,time,message,topicname);
    }

    @Override
    public String toString()
    {
        return topicname+"@@@"+peername+"@@@"+time+"@@@"+message;
    }

}
