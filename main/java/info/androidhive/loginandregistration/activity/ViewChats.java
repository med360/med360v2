package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.adapter.BookingListAdapter;
import info.androidhive.loginandregistration.adapter.ChatListAdapter;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Appointment;
import info.androidhive.loginandregistration.model.Chats;

public class ViewChats extends AppCompatActivity {


    private ChatListAdapter adapter;
    private SessionManager session;
    private List<Chats> chatList = new ArrayList<Chats>();
    private static final String chatsurl = "http://azmediame.net/med360/webinterface/api/get_message.php";

    private ProgressDialog pDialog;
    private ListView listView;
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(ViewChats.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(ViewChats.this,
                            ViewBooking.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.apointment:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent5 = new Intent(ViewChats.this,
                            ViewChats.class);
                    startActivity(intent5);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(ViewChats.this,
                            MainActivity.class);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.logoutnav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    session = new SessionManager(getApplicationContext());
                    session.setLogin(false);
                    session.logoutUser();
                    return true;

            }
            return false;
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chats);
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = session.getUserDetails();
        String pid = user.get("pid");
        final String userid = user.get("userid");
        Log.e("viewbook", "Session PID is: "+pid);
        Log.e("viewbook", "Session USER DETAILS IN HASHMAP RETURNED BY SESSION is: "+user);
        Log.e("viewbook", "Session USERID is: "+userid);

        listView = (ListView) findViewById(R.id.chlist);
        adapter = new ChatListAdapter(this, chatList);
        listView.setAdapter(adapter);


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                //String reqid = ((TextView) view.findViewById(R.id.reqid)).getText()
                       // .toString();
               String chpuid = ((TextView) view.findViewById(R.id.chpuid)).getText()
                        .toString();
              String chconvowith = ((TextView) view.findViewById(R.id.chconvowith)).getText()
                      .toString();
                String chduid = ((TextView) view.findViewById(R.id.chduid)).getText()
                     .toString();
                //String bkprefinfo = ((TextView) view.findViewById(R.id.bkprefinfo)).getText()
                       // .toString();
                //String bkstatusinfo = ((TextView) view.findViewById(R.id.statusinfo)).getText()
                        //.toString();
                //String bkthumbnail = ((TextView) view.findViewById(R.id.bkimagelink)).getText().toString();

                //String hpaddress = ((TextView) view.findViewById(R.id.bkhpaddress)).getText().toString();
                //String hospitalname = ((TextView) view.findViewById(R.id.bkhospitalname)).getText().toString();

                //Log.e("hp", "received hp address from listview : "+hpaddress);

                // Starting new intent
             Intent in = new Intent(getApplicationContext(),
                       ConversationActivity.class);
                // sending pid to next activity
               in.putExtra("puid", userid);
              in.putExtra("duid", chconvowith);
                //in.putExtra("bktimeinfo", bktimeinfo);
                //in.putExtra("bkprefinfo", bkprefinfo);
               // in.putExtra("bkstatusinfo", bkstatusinfo);
                //in.putExtra("bkthumbnail", bkthumbnail);
                //in.putExtra("bkhpaddress", hpaddress);
                //in.putExtra("bkhospitalname", hospitalname);




                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
       });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        get_chats(userid);

    }







    public void get_chats(final String userid){
        Log.e("viewbook", "before volley request");
        // changing action bar color


        // Creating volley request obj
        adapter.clearData();
        adapter.notifyDataSetChanged();
        String tag_string_req = "req_login";
        Log.e("viewbook", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(chatsurl);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                chatsurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hidePDialog();
                try {

                    Log.e("viewbook", "inside onresponse- Response String is: "+response);





                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        JSONArray chatsarray = jObj.getJSONArray("row");


                        Log.e("viewbook", "inside onresponse Array is: " + chatsarray);
// Parsing json
                        for (int i = 0; i < chatsarray.length(); i++) {
                            try {
                                Log.e("viewbook", "inside response loop");
                                JSONObject obj = chatsarray.getJSONObject(i);
                                Chats chat = new Chats();
                                chat.setDname(obj.getString("dname"));
                                chat.setThumbnailUrl(obj.getString("image"));

                                chat.setPuid(obj.getString("puid"));
                                chat.setDuid(obj.getString("duid"));
                                chat.setMssg(obj.getString("mssg"));
                                chat.setMsgid(obj.getString("mid"));
                                chat.setStime(obj.getString("sendtime"));
                                chat.setConvowith(obj.getString("convowith"));


                                chatList.add(chat);
                                Log.e("viewbook", "added to list");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        // Error in login. Get the error message
                        Log.e("viewbook", "error in listing doctor");
                        String errorMsg = jObj.getString("message");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Doctor receive Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Log.e("viewbook", "inside getparams method ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
    }
}
