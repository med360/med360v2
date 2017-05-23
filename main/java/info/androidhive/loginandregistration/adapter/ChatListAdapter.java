package info.androidhive.loginandregistration.adapter;

/**
 * Created by Siya on 3/30/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.model.Appointment;
import info.androidhive.loginandregistration.model.Chats;


public class ChatListAdapter extends BaseAdapter {
    private Activity activity;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;
    private List<Chats> chatitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ChatListAdapter(Activity activity, List<Chats> chatitems) {
        this.activity = activity;
        this.chatitems = chatitems;
    }

    public void clearData() {
        // clear the data
        chatitems.clear();
    }

    @Override
    public int getCount() {
        return  chatitems.size();
    }

    @Override
    public Object getItem(int location) {
        return  chatitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.chat_row, null);



        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.chthumbnail);
        TextView chdname = (TextView) convertView.findViewById(R.id.chdname);
        TextView chlast = (TextView) convertView.findViewById(R.id.chlast);
        TextView chpuid = (TextView) convertView.findViewById(R.id.chpuid);
        TextView chduid = (TextView) convertView.findViewById(R.id.chduid);
        TextView chimagelink = (TextView) convertView.findViewById(R.id.chimagelink);
        TextView chtime = (TextView) convertView.findViewById(R.id.chtime);
        TextView convowith = (TextView) convertView.findViewById(R.id.chconvowith);

        // getting movie data for the row
        Chats ch =  chatitems.get(position);

        String chimglink=ch.getThumbnailUrl();

        Log.e("chatadapter", "1. imageurl returned by getthumbnailurl() is " + chimglink);
        thumbNail.setImageUrl(ch.getThumbnailUrl(), imageLoader);


        chdname.setText(ch.getDname());
        chpuid.setText(ch.getPuid());
        chduid.setText(ch.getDuid());
        chlast.setText(ch.getMssg());
chtime.setText(ch.getStime());
        convowith.setText(ch.getConvowith());




       chimagelink.setText(ch.getThumbnailUrl());

        chimglink=ch.getThumbnailUrl();

       

        return convertView;
    }





}