package anilbehera.p2pchat.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import anilbehera.p2pchat.info.MessageInfo;
import anilbehera.p2pchat.info.RegistryInfo;

/**
 * Created by Anil Behera on 8/10/2015.
 *
 * Accepts Single TCP connection and process information
 */
public class TCPSender extends Thread{

    private InetAddress mAddress;
    private String message;

    TCPSender(InetAddress mAddress,String message)
    {
        this.mAddress=mAddress;
        this.message=message;
    }
    @Override
    public void run()
    {
        Socket mSocket;
        if (!mAddress.equals("0.0.0.0")) {
            try {

                mSocket = new Socket(mAddress, RegistryInfo.TCP_PORT);
                OutputStream outStream = mSocket.getOutputStream();
                int size = message.getBytes().length;
                byte sizeData[] = new byte[4];
                int temp_int = size;
                for (int i = 0; i < sizeData.length; i++) {
                    sizeData[i] = Integer.valueOf(temp_int & 0xff).byteValue();
                    temp_int = temp_int >> 8;
                }
                outStream.write(sizeData);
                outStream.write(message.getBytes());
                outStream.close();
                mSocket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
