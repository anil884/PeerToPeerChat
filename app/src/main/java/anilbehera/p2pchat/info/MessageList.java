package anilbehera.p2pchat.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Behera on 8/9/2015.
 *
 * Creates a collection of Messages for each chat room
 */
public class MessageList {
    public String topicname;
    public List<MessageInfo> messages=new ArrayList<MessageInfo>();

    MessageList(String topicname)
    {
        this.topicname=topicname;
    }

    public synchronized void  addmessage(MessageInfo i)
    {
        messages.add(i);
    }
    public synchronized  List<MessageInfo> getmessages()
    {
        return messages;
    }

    public static MessageList createInstance(String name)
    {
        return new MessageList(name);
    }

}
