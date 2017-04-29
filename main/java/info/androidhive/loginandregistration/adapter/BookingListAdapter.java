package info.androidhive.loginandregistration.adapter;

/**
 * Created by Siya on 3/30/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.Booking;
import info.androidhive.loginandregistration.activity.DoctorProfile;
import info.androidhive.loginandregistration.activity.ViewBooking;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.model.Appointment;


public class BookingListAdapter extends BaseAdapter {
    private Activity activity;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;
    private List<Appointment> bookingItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public BookingListAdapter(Activity activity, List<Appointment> bookingItems) {
        this.activity = activity;
        this.bookingItems = bookingItems;
    }

    public void clearData() {
        // clear the data
        bookingItems.clear();
    }

    @Override
    public int getCount() {
        return  bookingItems.size();
    }

    @Override
    public Object getItem(int location) {
        return  bookingItems.get(location);
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
            convertView = inflater.inflate(R.layout.booklist_row, null);



        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.bkthumbnail);
        TextView date = (TextView) convertView.findViewById(R.id.bkdate);
        TextView timeinfo = (TextView) convertView.findViewById(R.id.bktimeinfo);
        TextView prefinfo = (TextView) convertView.findViewById(R.id.bkprefinfo);
        TextView statusinfo = (TextView) convertView.findViewById(R.id.statusinfo);
        TextView reqid = (TextView) convertView.findViewById(R.id.reqid);

        TextView bkhpaddress = (TextView) convertView.findViewById(R.id.bkhpaddress);
        TextView bkhospitalname=(TextView) convertView.findViewById(R.id.bkhospitalname);
        TextView bkimagelink = (TextView) convertView.findViewById(R.id.bkimagelink);

        // getting movie data for the row
        Appointment m =  bookingItems.get(position);


        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);


        date.setText(m.getDate());



bkhpaddress.setText(m.getHpaddress());
        bkhospitalname.setText(m.getHospitalname());
        bkimagelink.setText(m.getThumbnailUrl());
        timeinfo.setText(""+m.getCtime());


        prefinfo.setText(m.getPrefe());

      String stinfo=m.getStatus();
        if(stinfo.equals("PENDING")){

            statusinfo.setTextColor(Color.parseColor("#ffa800"));

        }
            else if(stinfo.equals("CONFIRMED")){
            statusinfo.setTextColor(Color.parseColor("#ffa800"));

            statusinfo.setTextColor(Color.parseColor("#057100"));
        }
        else if(stinfo.equals("REJECTED")){

            statusinfo.setTextColor(Color.parseColor("#ff0000"));
      }
       else if(stinfo.equals("CANCELLED")){

           statusinfo.setTextColor(Color.parseColor("#ff0000"));
        }

            statusinfo.setText(m.getStatus());
        // doctor id
        reqid.setText(m.getReqid());

        final String requestid=m.getReqid();


        return convertView;
    }





}