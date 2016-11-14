package anilbehera.p2pchat.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/9/2015.
 * Creates TCP server to accept multiple TCP connections
 */
public class TCPReciverServer extends Thread {

    private boolean stopFlag=true;
    public ServerSocket server;
    public static Vector<TCPReciver> connections=new Vector<TCPReciver>();

    TCPReciverServer()
    {
        try {
            server = new ServerSocket(RegistryInfo.TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run()
    {
        try {
            while (stopFlag) {
                if(RegistryInfo.macaddress==null || RegistryInfo.chatname == null)
                {
                    finish();
                    continue;
                }
                Socket socket = server.accept();
                TCPReciver reciver = new TCPReciver(socket);
                reciver.start();

                if(socket!=null)
                {
                    synchronized(connections){
                        connections.addElement(reciver);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void finish() {
        for (int i = 0; i < connections.size(); i++) {
            try {
                connections.get(i).finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connections.clear();
    }

}
