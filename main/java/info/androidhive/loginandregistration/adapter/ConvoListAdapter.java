package info.androidhive.loginandregistration.adapter;

/**
 * Created by Siya on 3/30/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.model.Chats;
import info.androidhive.loginandregistration.model.Convos;


public class ConvoListAdapter extends BaseAdapter {
    private Activity activity;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;
    private List<Convos> convoitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ConvoListAdapter(Activity activity, List<Convos> convoitems) {
        this.activity = activity;
        this.convoitems = convoitems;
    }

    public void clearData() {
        // clear the data
        convoitems.clear();
    }

    @Override
    public int getCount() {
        return convoitems.size();
    }

    @Override
    public Object getItem(int location) {
        return  convoitems.get(location);
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
            convertView = inflater.inflate(R.layout.con_row, null);



       // if (imageLoader == null)
      //      imageLoader = AppController.getInstance().getImageLoader();
      //  NetworkImageView thumbNail = (NetworkImageView) convertView
      //          .findViewById(R.id.chthumbnail);
        TextView cnsndname = (TextView) convertView.findViewById(R.id.cnsndname);
        EditText cnmssg = (EditText) convertView.findViewById(R.id.cnmssg);



        // getting movie data for the row
        Convos cn =  convoitems.get(position);






        cnsndname.setText(cn.getCnsndname());
        cnmssg.setText(cn.getCnmssg());






       

        return convertView;
    }





}