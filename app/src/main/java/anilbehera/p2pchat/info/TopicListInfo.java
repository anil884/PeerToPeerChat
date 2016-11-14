package anilbehera.p2pchat.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Behera on 8/10/2015.
 *
 * Maintains a collection of chat rooms and their meta data information
 */
public class TopicListInfo {
    private  List<String> TopicList=new ArrayList<String>();

    public synchronized void addTopic(String s)
    {
        TopicList.add(s);
    }
    public synchronized  boolean contains(String s)
    {
        return TopicList.contains(s);
    }
    public synchronized List<String> getTopicList()
    {
        return TopicList;
    }
    public synchronized void removeTopic(String s)
    {
        TopicList.remove(s);
    }



}
