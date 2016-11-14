package anilbehera.p2pchat.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import anilbehera.p2pchat.info.MessageInfo;
import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/9/2015.
 * Implements message recieving using TCP connection
 */
public class TCPReciver extends Thread {

    private Socket socket;
    private InputStream inputStream;
    private static String TAG = "TCPREciver";

    TCPReciver(Socket s)
    {
        try {
            socket = s;
            inputStream = s.getInputStream();
        } catch(IOException e)
        {
            //e.printStackTrace();
            Log.v(TAG,"Socket Error, Sorry Boss!");

        }
    }

    @Override
    public void run() {
            while(true)
            {
                try{
                    int res = 0;
                    int size = 0;
                    while (size == 0) {
                        byte sizeData[] = new byte[4];
                        res = inputStream.read(sizeData);
                        size = ((((sizeData[3] & 0xff) << 24) | ((sizeData[2] & 0xff) << 16)
                                | ((sizeData[1] & 0xff) << 8) | ((sizeData[0] & 0xff) << 0)));
                    }
                    byte data[] = new byte[size];
                    int isRead = 0;
                    byte buffer[] = new byte[1024];
                    while (isRead < size) {
                        res = inputStream.read(buffer, 0, buffer.length);
                        System.arraycopy(buffer, 0, data, isRead, res);
                        isRead += res;
                    }
                    String str[] = new String(data,"UTF-8").split("@@@");
                    Log.v("MultiReciver","Msg:"+new String(data,"UTF-8"));
                    String topicname=str[0];
                    MessageInfo msg = MessageInfo.createInstance(str[1],str[2],str[3],topicname);
                    if(RegistryInfo.RegisteredTopicList.contains(topicname))
                        RegistryInfo.TopicMessageInfo.get(topicname).addmessage(msg);

                    SendMessage(topicname);


                }catch (IOException e)
                {
                    Log.v(TAG,"Socket Error, Sorry Boss!");
                }

            }

    }

    private void SendMessage(String topicname) {
       // Log.i(TAG,"Coming Here");
       if(RegistryInfo.topicname.equalsIgnoreCase(topicname) && RegistryInfo.chatpageHandler!=null) {
           Message msg = new Message();
           msg.what = 1;
           RegistryInfo.chatpageHandler.sendMessage(msg);
           //Log.i(TAG, "Message Sent");
       }
    }

    public void finish() throws IOException {
        if (socket!=null && !socket.isClosed()) {
            socket.close();
        }
        if(inputStream!=null)
            inputStream.close();
    }
}
