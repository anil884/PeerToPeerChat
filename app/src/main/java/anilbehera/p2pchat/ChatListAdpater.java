package anilbehera.p2pchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import anilbehera.p2pchat.info.MessageInfo;

/**
 * Created by Anil Behera on 8/10/2015.
 * Coustomized Adapter to display each chat messages
 */
public class ChatListAdpater  extends ArrayAdapter {


    private int resource;
    private LayoutInflater inflater;
    private Context context;
    final private String dark="#000000";
    final private String light="#ffffff";
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatListAdpater(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.context=context;
        inflater = LayoutInflater.from( context );

    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent )
    {
        convertView = (LinearLayout) inflater.inflate( resource, null );
        MessageInfo info = (MessageInfo)getItem( position );
        TextView txtName = (TextView) convertView.findViewById(R.id.tv_chatcontent);
        txtName.setText(info.message);
        TextView peerName = (TextView) convertView.findViewById(R.id.tv_chatname);
        peerName.setText(info.peername+":");
        TextView time = (TextView) convertView.findViewById(R.id.tv_time);
        time.setText(info.time.toString());


        return convertView;
    }
}
